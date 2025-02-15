package at.flori4n.mcmatchmakingsolo.states.inGame;

import at.flori4n.mcmatchmakingsolo.GameData;
import at.flori4n.mcmatchmakingsolo.states.gameOver.GameOverState;
import at.flori4n.mcmatchmakingsolo.Manager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class InGameListeners implements Listener {
    GameData gameData = GameData.getInstance();

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
    public void playerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(gameData.getLobbyLocation());
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        gameData.getPlayers().remove(event.getPlayer());
        if (gameData.getPlayers().size()==1){
            Manager.getInstance().setState(new GameOverState(gameData.getPlayers().get(0)));
        }
    }

    @EventHandler
    public void foodListener(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e){
        gameData.getPlayers().remove(e.getEntity());
        if (gameData.getPlayers().size()==1){
            Manager.getInstance().setState(new GameOverState(gameData.getPlayers().get(0)));
        }
    }
}
