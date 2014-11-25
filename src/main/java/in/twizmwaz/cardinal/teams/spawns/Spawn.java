package in.twizmwaz.cardinal.teams.spawns;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.point.PointRegion;

import java.util.List;
import java.util.Random;

/**
 * Created by kevin on 11/21/14.
 */
public class Spawn {

    private int yaw;
    private List<Region> regions;


    public Spawn(List<Region> regions, int yaw) {
        this.regions = regions;
        this.yaw = yaw;
    }

    public PointRegion getPoint() {

        Random random = new Random();
        PointRegion point = regions.get((int) (Math.random() * regions.size())).getRandomPoint();

        return new PointRegion(point.getX(), point.getY(), point.getZ(), this.yaw, 0);

    }


}
