package at.flori4n.mcmatchmakingsolo.stats;

import java.util.UUID;

/** Per-player stats for one match. placement 0 = undecided. */
public class PlayerStat {

    private final UUID playerId;
    private int kills;
    private int placement;

    public PlayerStat(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }
}
