package at.flori4n.mcmatchmakingsolo.states.inGame;

import at.flori4n.mcmatchmakingsolo.GameData;
import at.flori4n.mcmatchmakingsolo.states.gameOver.GameOverState;
import at.flori4n.mcmatchmakingsolo.Manager;
import at.flori4n.mcmatchmakingsolo.stats.StatsService;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
        StatsService.getInstance().applyBoardTo(player);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        StatsService.getInstance().setPlacement(event.getPlayer().getUniqueId(), gameData.getPlayers().size());
        gameData.getPlayers().remove(event.getPlayer());
        if (gameData.getPlayers().size()==1){
            StatsService.getInstance().setPlacement(gameData.getPlayers().get(0).getUniqueId(), 1);
            Manager.getInstance().setState(new GameOverState(gameData.getPlayers().get(0)));
        }
    }

    @EventHandler
    public void foodListener(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e){
        Player killer = e.getEntity().getKiller();
        if (killer != null) {
            StatsService.getInstance().recordKill(killer.getUniqueId());
        }
        StatsService.getInstance().setPlacement(e.getEntity().getUniqueId(), gameData.getPlayers().size());
        gameData.getPlayers().remove(e.getEntity());
        e.getEntity().setGameMode(GameMode.SPECTATOR);
        if (gameData.getPlayers().size()==1){
            StatsService.getInstance().setPlacement(gameData.getPlayers().get(0).getUniqueId(), 1);
            Manager.getInstance().setState(new GameOverState(gameData.getPlayers().get(0)));
        }
    }
}
