package at.flori4n.mcmatchmakingsolo.states.commands;


import at.flori4n.mcmatchmakingsolo.Manager;
import at.flori4n.mcmatchmakingsolo.states.inGame.InGameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (player.hasPermission("game.start")) Manager.getInstance().setState(new InGameState());
        return false;
    }
}
