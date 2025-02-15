package at.flori4n.mcmatchmakingsolo;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameData {

    private static GameData instance;

    @Getter
    private List<Location> spawns = new ArrayList<Location>();
    @Getter
    private List<Player> players = new ArrayList<>();
    @Getter
    @Setter
    private float borderStartingSize,borderDamage;
    @Getter
    @Setter
    private long borderShrinkingSpeed;
    @Getter
    @Setter
    private Location borderCenter;
    @Getter
    @Setter
    private float borderMovementSpeed;
    @Getter
    private List<Location> borderEndPoints = new ArrayList<>();
    @Getter
    @Setter
    private boolean useBorder,start;
    @Getter
    @Setter
    private int lobbyTime,playersToStart;
    @Getter
    @Setter
    private Location lobbyLocation;



    private void load(){
        FileConfiguration config = McMatchmakingSolo.getPlugin().getConfig();

        start = config.getBoolean("start");
        List<Location> spawns = (List<Location>)config.getList("spawns");
        if (spawns!=null)this.spawns = spawns;
        lobbyLocation = (Location) config.get("lobbyLocation");
        lobbyTime = config.getInt("lobbyTime");
        playersToStart = config.getInt("playersToStart");
        //border config
        useBorder = config.getBoolean("useBorder");
        borderStartingSize = config.getLong("borderStartingSize");
        borderDamage = (float) config.getDouble("borderDamage");
        borderShrinkingSpeed = (long) config.getDouble("borderShrinkingSpeed");
        borderCenter = (Location) config.get("borderCenter");
        borderMovementSpeed = (float) config.getDouble("borderMovementSpeed");
        List<Location> borderEndPoints = (List<Location>)config.get("borderEndPoints");
        if (borderEndPoints!=null)this.borderEndPoints = borderEndPoints;
    }

    public void save(){
        FileConfiguration config = McMatchmakingSolo.getPlugin().getConfig();
        config.set("spawns", spawns);
        config.set("borderStartingSize", borderStartingSize);
        config.set("borderDamage", borderDamage);
        config.set("borderShrinkingSpeed", borderShrinkingSpeed);
        config.set("borderCenter", borderCenter);
        config.set("borderMovementSpeed", borderMovementSpeed);
        config.set("borderEndPoints", borderEndPoints);
        config.set("useBorder", useBorder);
        config.set("start", start);
        config.set("lobbyLocation", lobbyLocation);
        config.set("lobbyTime", lobbyTime);
        config.set("playersToStart", playersToStart);
        McMatchmakingSolo.getPlugin().saveConfig();
    }

    private GameData() {
        load();
    }

    public int getMaxPlayers(){
        return spawns.size();
    }

    public static GameData getInstance() {
        if (instance == null) instance = new GameData();
        return instance;
    }
}
