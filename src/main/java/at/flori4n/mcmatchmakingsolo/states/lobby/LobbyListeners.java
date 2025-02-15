package at.flori4n.mcmatchmakingsolo.states.lobby;

import at.flori4n.mcmatchmakingsolo.GameData;
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

public class LobbyListeners implements Listener {
    private int taskId;
    private GameData gameData = GameData.getInstance();
    private LobbyState lobbyState;


    public LobbyListeners(LobbyState lobbyState){
        this.lobbyState = lobbyState;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        p.teleport(gameData.getLobbyLocation());
        p.getInventory().clear();
        p.setGameMode(GameMode.SURVIVAL);

        if (gameData.getPlayers().size()<gameData.getMaxPlayers()){
            gameData.getPlayers().add(event.getPlayer());
        }
        if (!lobbyState.isTaskRunning()&&Bukkit.getOnlinePlayers().size()>=gameData.getPlayersToStart()){
            lobbyState.startCounter();
        }
        Bukkit.broadcastMessage(Bukkit.getOnlinePlayers().size() +"/"+gameData.getMaxPlayers() + " Spieler");

    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        gameData.getPlayers().remove(p);

        if (lobbyState.isTaskRunning()&&Bukkit.getOnlinePlayers().size()-1<gameData.getPlayersToStart()){
            lobbyState.stopCounter();
            Bukkit.broadcastMessage("Start abgebrochen");
            Bukkit.broadcastMessage("Zu wenig Spieler");
        }
        Bukkit.broadcastMessage(Bukkit.getOnlinePlayers().size()-1 +"/"+gameData.getMaxPlayers() + " Spieler");
    }

    @EventHandler
    public void foodListener(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void damageListener(EntityDamageEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void blockBreakListener(BlockBreakEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void weatherChangeListener(WeatherChangeEvent e){
        e.setCancelled(true);
        e.getWorld().setThundering(false);
    }
    @EventHandler
    public void onServerPing(ServerListPingEvent e){
        e.setMotd("Lobby");
    }
}
