package in.twizmwaz.cardinal.teams.spawns;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.type.PointRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class Spawn {

    private int yaw;
    private List<Region> regions;
    private String kit;
    private Vector direction;

    /**
     * @param regions Regions for the spawn
     * @param yaw     Yaw for a player when spawned
     * @param kit     Kit to be given to the player when spawned
     */
    public Spawn(List<Region> regions, int yaw, String kit) {
        if (regions.size() == 0) Bukkit.getLogger().log(Level.SEVERE, "Failed to load spawns");
        this.regions = regions;
        this.yaw = yaw;
        this.kit = kit;
    }

    /**
     * @param regions   Regions for the spawn
     * @param direction Direction the player should face when spawned
     * @param kit       Kit to be given to the player when spawned
     */
    public Spawn(List<Region> regions, Vector direction, String kit) {
        if (regions.size() == 0) Bukkit.getLogger().log(Level.SEVERE, "Failed to load spawns");
        this.regions = regions;
        this.direction = direction;
        this.kit = kit;
    }

    /**
     * @return Returns a random PointRegion in the spawn
     */
    public PointRegion getPoint() {
        Random random = new Random();
        Location location = regions.get(random.nextInt(regions.size())).getRandomPoint().getLocation();
        if (!(direction == null)) {
            location.setDirection(direction);
            return new PointRegion(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }
        return new PointRegion(location.getX(), location.getY(), location.getZ(), this.yaw, 0);
    }

    public Location getLocation() {
        Random random = new Random();
        PointRegion pointRegion = regions.get(random.nextInt(regions.size())).getRandomPoint();
        Location location = new Location(GameHandler.getGameHandler().getMatchWorld(), pointRegion.getX(), pointRegion.getY(), pointRegion.getZ(), yaw, 0);
        if (direction != null) {
            location.setDirection(direction);
            return location;
        }
        return location;
    }

    /**
     * @return Returns the kit assigned to the spawn
     */
    public Kit getKit() {
        return Kit.getKitByName(kit);
    }
}
