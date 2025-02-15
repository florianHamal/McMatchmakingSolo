package at.flori4n.mcmatchmakingsolo;

import at.flori4n.mcmatchmakingsolo.states.commands.Commands;
import at.flori4n.mcmatchmakingsolo.states.commands.StartCommand;
import at.flori4n.mcmatchmakingsolo.states.lobby.LobbyState;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class McMatchmakingSolo extends JavaPlugin {
    @Getter
    private static McMatchmakingSolo plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getCommand("mcMatchmaking").setExecutor(new Commands());
        if (GameData.getInstance().isStart()){
            Manager.getInstance().setState(new LobbyState());
            getCommand("start").setExecutor(new StartCommand());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
