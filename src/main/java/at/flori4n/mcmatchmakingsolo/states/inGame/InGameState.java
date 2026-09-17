package at.flori4n.mcmatchmakingsolo.states.inGame;

import at.flori4n.mcmatchmakingsolo.BorderManager;
import at.flori4n.mcmatchmakingsolo.GameData;
import at.flori4n.mcmatchmakingsolo.McMatchmakingSolo;
import at.flori4n.mcmatchmakingsolo.State;
import at.flori4n.mcmatchmakingsolo.stats.StatsService;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.Random;

public class InGameState implements State {
    InGameListeners inGameListeners = new InGameListeners();
    @Override
    public void preaction() {
        System.out.println("initIngGmeState");
        Bukkit.getPluginManager().registerEvents(inGameListeners, McMatchmakingSolo.getPlugin());

        for (Player player:Bukkit.getOnlinePlayers()){
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
        }

        Random random = new Random();
        GameData gameData = GameData.getInstance();
        gameData.getPlayers().forEach(player -> {
            Location loc = gameData.getSpawns().get(random.nextInt(gameData.getSpawns().size()));
            gameData.getSpawns().remove(loc);
            player.teleport(loc);
            player.setGameMode(GameMode.SURVIVAL);
        });

        String matchName = McMatchmakingSolo.getPlugin().getConfig()
                .getString("cloudpanel.match-name", "mc-match");
        StatsService.getInstance().start(new java.util.ArrayList<>(gameData.getPlayers()), matchName);

        if (GameData.getInstance().isUseBorder()){
            BorderManager.getInstance().start();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        }
    }

    @Override
    public void action() {
        System.out.println("runningInGameState");
    }

    @Override
    public void postAction() {
        System.out.println("stopInGameState");
        StatsService.getInstance().stopAndUpload();
        HandlerList.unregisterAll(inGameListeners);
    }
}
