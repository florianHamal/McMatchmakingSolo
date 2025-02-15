package at.flori4n.mcmatchmakingsolo.states.commands;

import at.flori4n.mcmatchmakingsolo.GameData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    GameData gameData = GameData.getInstance();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!player.hasPermission("setup"))return false;
        try {
            switch (strings[0]) {
                case "help":
                    player.sendMessage("--------help--------\n"+
                            "/mcMatchmaking addSpawn\n"+
                            "/mcMatchmaking configBorder <startingSize> <damage> <shrinkingSpeed> <movementSpeed>\n"+
                            "/mcMatchmaking setBorderCenter\n"+
                            "/mcMatchmaking useBorder <true/false>\n" +
                            "/mcMatchmaking setStart <true/false>\n"+
                            "/mcMatchmaking addBorderEndPoint\n"+
                            "/mcMatchmaking setLobby <lobbyTime> <playersToStart> (usingPlayerLocation)\n"+
                            "--------------------");
                    break;
                case "addSpawn":
                    gameData.getSpawns().add(player.getLocation());
                    break;
                case "configBorder":
                    gameData.setBorderStartingSize(Float.parseFloat(strings[1]));
                    gameData.setBorderDamage(Float.parseFloat(strings[2]));
                    gameData.setBorderShrinkingSpeed(Long.parseLong(strings[3]));
                    gameData.setBorderMovementSpeed(Float.parseFloat(strings[4]));
                    break;
                case "setBorderCenter":
                    gameData.setBorderCenter(player.getLocation());
                    break;
                case "useBorder":
                    gameData.setUseBorder(Boolean.parseBoolean(strings[1]));
                    break;
                case "setStart":
                    gameData.setStart(Boolean.parseBoolean(strings[1]));
                    break;
                case "addBorderEndPoint":
                    gameData.getBorderEndPoints().add(player.getLocation());
                    break;
                case "setLobby":
                    gameData.setLobbyLocation(player.getLocation());
                    gameData.setLobbyTime(Integer.parseInt(strings[1]));
                    gameData.setPlayersToStart(Integer.parseInt(strings[2]));
                    break;
                case "save":
                    gameData.save();
                    break;
                default:
                    player.sendMessage("wrong Command");
            }
        }catch (RuntimeException e){
            player.sendMessage(e.getMessage());
        }
        return false;
    }
}
