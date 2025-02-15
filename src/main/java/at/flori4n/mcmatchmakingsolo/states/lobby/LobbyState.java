package at.flori4n.mcmatchmakingsolo.states.lobby;


import at.flori4n.mcmatchmakingsolo.*;
import at.flori4n.mcmatchmakingsolo.states.inGame.InGameState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LobbyState implements State {

    private final LobbyListeners lobbyListeners = new LobbyListeners(this);
    private int taskId;
    @Getter
    private boolean taskRunning = false;

    @Override
    public void preaction() {
        System.out.println("initLobbyState");
        Bukkit.getPluginManager().registerEvents(lobbyListeners, McMatchmakingSolo.getPlugin());
    }

    @Override
    public void action() {
        System.out.println("runningLobbyState");
    }

    @Override
    public void postAction() {
        System.out.println("stopLobbyState");
        HandlerList.unregisterAll(lobbyListeners);
        stopCounter();
    }

    public void startCounter(){
        taskRunning = true;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(McMatchmakingSolo.getPlugin(), new Runnable() {
            int counter = GameData.getInstance().getLobbyTime();
            @Override
            public void run() {

                if (counter%10 == 0 && counter>0){
                    Bukkit.broadcastMessage("Runde startet in "+counter+" Sekunden");
                    playSound();
                }
                if (counter<5){
                    Bukkit.broadcastMessage("Runde startet in "+counter+" Sekunden");
                    playSound();
                }
                if (counter<=0){
                    Manager.getInstance().setState(new InGameState());
                }
                counter --;
            }
            public void playSound(){
                for (Player player:Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM,1,1);
                }
            }
        }, 0, 20);

    }

    public void stopCounter(){
        Bukkit.getScheduler().cancelTask(taskId);
        taskRunning = false;
    }


}
