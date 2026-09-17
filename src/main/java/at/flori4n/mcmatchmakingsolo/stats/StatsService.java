package at.flori4n.mcmatchmakingsolo.stats;

import at.flori4n.cloudPanel.api.MatchPutRequest;
import at.flori4n.cloudPanel.api.StatisticAddRequest;
import at.flori4n.cloudPanel.client.BlockingCloudPanelClient;
import at.flori4n.mcmatchmakingsolo.McMatchmakingSolo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Tracks kills and placements for the running match and uploads them to the
 * CloudPanel API when the ingame state stops.
 *
 * <p>Placement rule: the leaving player gets the placement equal to the number of
 * players without a placement yet (including themselves), so the first death with
 * N players gets place N. Whoever is left without a placement when the match ends
 * gets place 1.
 */
public class StatsService {

    private static StatsService INSTANCE;

    private final Map<UUID, PlayerStat> stats = new LinkedHashMap<>();
    private UUID matchId;
    private String matchName;
    private LocalDateTime startTime;

    private StatsService() {
    }

    public static StatsService getInstance() {
        if (INSTANCE == null) INSTANCE = new StatsService();
        return INSTANCE;
    }

    public void start(List<Player> players, String matchName) {
        stats.clear();
        for (Player player : players) {
            stats.put(player.getUniqueId(), new PlayerStat(player.getUniqueId()));
        }
        this.matchId = UUID.randomUUID();
        this.matchName = matchName;
        this.startTime = LocalDateTime.now();
    }

    public void recordKill(UUID killerId) {
        PlayerStat stat = stats.get(killerId);
        if (stat != null) stat.setKills(stat.getKills() + 1);
    }

    public void setPlacement(UUID playerId, int placement) {
        PlayerStat stat = stats.get(playerId);
        if (stat != null) stat.setPlacement(placement);
    }

    public void stopAndUpload() {
        final MatchPutRequest request = new MatchPutRequest(matchId, matchName, startTime, LocalDateTime.now(), toStatisticRequests());
        final String baseUrl = McMatchmakingSolo.getPlugin().getConfig()
                .getString("cloudpanel.url", "http://localhost:8080");
        Bukkit.getScheduler().runTaskAsynchronously(McMatchmakingSolo.getPlugin(),
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new BlockingCloudPanelClient(baseUrl).putMatch(request);
                        } catch (Exception e) {
                            McMatchmakingSolo.getPlugin().getLogger().log(Level.WARNING,
                                    "Could not upload match stats to CloudPanel (" + baseUrl + "): "
                                            + e.getMessage());
                        }
                    }
                });
    }

    private List<StatisticAddRequest> toStatisticRequests() {
        List<StatisticAddRequest> requests = new ArrayList<>();
        for (PlayerStat stat : stats.values()) {
            requests.add(new StatisticAddRequest(stat.getPlayerId(), stat.getPlacement(), stat.getKills()));
        }
        return requests;
    }
}
