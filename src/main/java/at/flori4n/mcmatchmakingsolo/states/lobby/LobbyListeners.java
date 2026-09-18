package at.flori4n.mcmatchmakingsolo.states.lobby;

import at.flori4n.cloudPanel.api.PlayerJoinRequest;
import at.flori4n.cloudPanel.client.BlockingCloudPanelClient;
import at.flori4n.mcmatchmakingsolo.GameData;
import at.flori4n.mcmatchmakingsolo.McMatchmakingSolo;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.github.paperspigot.Title;

import java.util.logging.Level;

public class LobbyListeners implements Listener {
	private GameData gameData = GameData.getInstance();
	private LobbyState lobbyState;

	public LobbyListeners(LobbyState lobbyState) {
		this.lobbyState = lobbyState;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		p.teleport(gameData.getLobbyLocation());
		p.getInventory().clear();
		p.setGameMode(GameMode.SURVIVAL);
			

		if(gameData.getFixedPlayers().isEmpty()){
			if (gameData.getPlayers().size() < gameData.getMaxPlayers()) {
				gameData.getPlayers().add(event.getPlayer());
			} else {
				p.sendTitle(new Title("Zu viele Spieler", "du bist Zuschauer"));
			}	
		}else{
			if(gameData.getFixedPlayers().contains(p.getName())){
				gameData.getPlayers().add(p);
			}else{
				p.sendTitle(new Title("Du bist Zuschauer"));
			}
		}


		
		if (!lobbyState.isTaskRunning() && gameData.getPlayers().size() >= gameData.getPlayersToStart()) {
			lobbyState.startCounter();
		}
		Bukkit.broadcastMessage(gameData.getPlayers().size() + "/" + gameData.getMaxPlayers() + " Spieler");
		refreshPlayerName(p);

	}

	/** Refreshes the player's name in CloudPanel on every lobby join. Runs async. No-op when disabled. */
	private void refreshPlayerName(final Player p) {
		final String baseUrl = gameData.getCloudPanelUrl();
		if (baseUrl == null || baseUrl.trim().isEmpty()) {
			return;
		}
		Bukkit.getScheduler().runTaskAsynchronously(McMatchmakingSolo.getPlugin(), new Runnable() {
			@Override
			public void run() {
				try {
					new BlockingCloudPanelClient(baseUrl)
							.joinPlayer(new PlayerJoinRequest(p.getUniqueId(), null, p.getName()));
				} catch (Exception e) {
					McMatchmakingSolo.getPlugin().getLogger().log(Level.WARNING,
							"Could not refresh player name in CloudPanel (" + baseUrl + "): " + e.getMessage());
				}
			}
		});
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (gameData.getPlayers().contains(player) && gameData.getFixedPlayers().isEmpty()) {
			Bukkit.getOnlinePlayers().stream()
				.filter(p -> gameData.getPlayers().contains(p)).findFirst()
				.ifPresent(p -> gameData.getPlayers().add(player));
		}
		gameData.getPlayers().remove(player);


		if (lobbyState.isTaskRunning() && gameData.getPlayers().size() - 1 < gameData.getPlayersToStart()) {
			lobbyState.stopCounter();
			Bukkit.broadcastMessage("Start abgebrochen");
			Bukkit.broadcastMessage("Zu wenig Spieler");
		}
		Bukkit.broadcastMessage(
				gameData.getPlayers().size() - 1 + "/" + gameData.getMaxPlayers() + " Spieler");
	}

	@EventHandler
	public void foodListener(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void damageListener(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void blockBreakListener(BlockBreakEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void weatherChangeListener(WeatherChangeEvent e) {
		e.setCancelled(true);
		e.getWorld().setThundering(false);
	}

	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		e.setMotd("Lobby");
	}
}
