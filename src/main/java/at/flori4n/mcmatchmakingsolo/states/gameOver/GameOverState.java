package at.flori4n.mcmatchmakingsolo.states.gameOver;

import at.flori4n.mcmatchmakingsolo.McMatchmakingSolo;
import at.flori4n.mcmatchmakingsolo.State;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.github.paperspigot.Title;

public class GameOverState implements State {

    private final GameOverListeners gameOverListeners = new GameOverListeners();

    private final Player winner;

    public GameOverState(Player winner){
        this.winner = winner;
    }
    @Override
    public void preaction() {
        Bukkit.getPluginManager().registerEvents(gameOverListeners, McMatchmakingSolo.getPlugin());
    }

    @Override
    public void action() {

        for (Player player: Bukkit.getOnlinePlayers()){
            player.sendTitle(new Title(ChatColor.GOLD +winner.getName(),"§2 hat gewonnen",1*20,10*20,1*20));
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + winner.getName() + ChatColor.GRAY + " hat gewonnen");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(McMatchmakingSolo.getPlugin(), new Runnable() {
            int counter = 20;
            @Override
            public void run() {
                counter --;
                if (counter<=0){
                    Bukkit.shutdown();
                }
            }
        }, 0, 20);

    }

    @Override
    public void postAction() {
        HandlerList.unregisterAll(gameOverListeners);
    }
}
