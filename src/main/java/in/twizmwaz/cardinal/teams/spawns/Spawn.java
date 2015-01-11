package in.twizmwaz.cardinal.teams.spawns;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.type.PointRegion;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class Spawn {

    private int yaw;
    private List<Region> regions;
    private String kit;
    private Vector direction;

    public Spawn(List<Region> regions, int yaw, String kit) {
        if (regions.size() == 0) Bukkit.getLogger().log(Level.SEVERE, "Failed to load spawns");
        this.regions = regions;
        this.yaw = yaw;
        this.kit = kit;
    }

    public PointRegion getPoint() {
        Random random = new Random();
        PointRegion point = regions.get(random.nextInt(regions.size())).getRandomPoint();
        return new PointRegion(point.getX(), point.getY(), point.getZ(), this.yaw, 0);
    }

    public Kit getKit() {
        return Kit.getKitByName(kit);
    }

    public boolean hasDirection() {
        return direction != null;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }
}
