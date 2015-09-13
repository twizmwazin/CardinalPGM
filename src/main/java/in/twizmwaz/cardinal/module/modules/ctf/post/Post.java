package in.twizmwaz.cardinal.module.modules.ctf.post;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.event.HandlerList;

import java.util.List;

public class Post implements Module {

    private PointRegion point;
    private String id;
    private TeamModule owner;
    private boolean permanent;          // Default: false
    private boolean sequential;         // Default: false
    private int pointsRate;             // Default: 0
    private FilterModule pickupFilter;
    private int recoverTime;            // Default: 30s
    private int respawnTime;
    private int respawnSpeed;           // Default: 8 (M/s)
    private float yaw;

    public Post(PointRegion point,
                String id,
                TeamModule owner,
                boolean permanent,
                boolean sequential,
                int pointsRate,
                FilterModule pickupFilter,
                int recoverTime,
                int respawnTime,
                int respawnSpeed,
                float yaw) {
        this.point = point;
        this.id = id;
        this.owner = owner;
        this.permanent = permanent;
        this.sequential = sequential;
        this.pointsRate = pointsRate;
        this.pickupFilter = pickupFilter;
        this.recoverTime = recoverTime;
        this.respawnTime = respawnTime;
        this.respawnSpeed = respawnSpeed;
        this.yaw = yaw;
    }

    public List<String> debug() {
        List<String> debug = Lists.newArrayList();
        debug.add("---------- DEBUG Post Flag ----------");
        debug.add("PointRegion point = " + point.getLocation().toString());
        debug.add("String id = " + id);
        debug.add("TeamModule owner = " + (owner == null ? "None" : owner.getName()));
        debug.add("boolean permanent = " + permanent);
        debug.add("boolean sequential = " + sequential);
        debug.add("int pointsRate = " + pointsRate);
        debug.add("FilterModule pickupFilter = " + (pickupFilter == null ? "None" : pickupFilter.getName()));
        debug.add("int recoverTime = " + recoverTime);
        debug.add("int respawnTime = " + respawnTime);
        debug.add("int respawnSpeed = " + respawnSpeed);
        debug.add("float yaw = " + yaw);
        debug.add("-------------------------------------");
        return debug;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public String getId() {
        return id;
    }

    public FilterModule getPickupFilter() {
        return pickupFilter;
    }
}
