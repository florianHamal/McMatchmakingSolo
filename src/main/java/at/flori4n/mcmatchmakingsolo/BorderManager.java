package at.flori4n.mcmatchmakingsolo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BorderManager {

    private static BorderManager instance;
    private final float startingSize,damage;
    private final long shrinkingSpeed;
    private int task;
    private final Location center;
    private final float movementSpeed;
    private final List<Location> endPoints;

    public void start() {
        int randInt  = new Random().nextInt(endPoints.size());
        Location endPoint = endPoints.get(randInt);
        World world = center.getWorld();
        world.getWorldBorder().setCenter(center);
        world.getWorldBorder().setDamageAmount(damage);
        //world.getWorldBorder().setSize(startingSize);
        world.getWorldBorder().setSize(startingSize);
        System.out.println(startingSize);
        System.out.println(shrinkingSpeed);
        world.getWorldBorder().setSize(startingSize, shrinkingSpeed);//close border


        //moveBorderToEndPoint
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(McMatchmakingSolo.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (center.getX()>endPoint.getX()){
                    if (center.getX()-endPoint.getX()< movementSpeed) center.setX(endPoint.getX()); //if its the last step
                    center.setX(center.getX()- movementSpeed);
                };
                if (center.getZ()>endPoint.getZ()){
                    if (center.getZ()-endPoint.getZ()< movementSpeed) center.setZ(endPoint.getZ());
                    center.setZ(center.getZ()- movementSpeed);
                };
                if (center.getX()<endPoint.getX()){
                    if (endPoint.getX()-center.getX()< movementSpeed) center.setX(endPoint.getX());
                    center.setX(center.getX()+ movementSpeed);
                };
                if (center.getZ()<endPoint.getZ()){
                    if (endPoint.getZ()-center.getZ()< movementSpeed) center.setZ(endPoint.getZ());
                    center.setZ(center.getZ()+ movementSpeed);
                };
                world.getWorldBorder().setCenter(center);
                if ((center.getX() - endPoint.getX()) == 0 && center.getZ() - endPoint.getZ() == 0){
                    Bukkit.getScheduler().cancelTask(task);
                }
            }
        },0,5);
    }

    private BorderManager (){
        GameData gameData = GameData.getInstance();
        this.startingSize = gameData.getBorderStartingSize();
        System.out.println(gameData.getBorderStartingSize()+"<----------");
        this.shrinkingSpeed = gameData.getBorderShrinkingSpeed();
        this.center = gameData.getBorderCenter();
        this.movementSpeed = gameData.getBorderMovementSpeed();
        this.endPoints = gameData.getBorderEndPoints();
        this.damage = gameData.getBorderDamage();

    }
    public static BorderManager getInstance(){
        if (instance == null)instance = new BorderManager();
        return instance;
    }
}
