package in.twizmwaz.cardinal.teams.spawns;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.point.PointRegion;
import in.twizmwaz.cardinal.util.NumUtils;

import java.util.List;

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

        PointRegion point = regions.get((int) NumUtils.randomInterval(0, regions.size() - 1)).getRandomPoint();
        return new PointRegion(point.getX(), point.getY(), point.getZ(), this.yaw, 0);

    }


}
