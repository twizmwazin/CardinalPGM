package in.twizmwaz.cardinal.module.modules.ctf.post;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import in.twizmwaz.cardinal.module.modules.ctf.event.PlayerPickupFlagEvent;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Random;

public class Post implements Module {

    private List<RegionModule> regions;
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

    private RegionModule currentRegion;
    private int lastRegionId = 0;

    public Post(List<RegionModule> regions,
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
        this.regions = regions;
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
        debug.add("RegionModule regions = " + regions.size());
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

    public List<RegionModule> getRegions() {
        return regions;
    }

    public RegionModule getNextFlagSpawn() {
        if (sequential) {
            if (lastRegionId >= getRegions().size()) lastRegionId = 0;
            return getRegions().get(lastRegionId ++);
        } else {
            Random rand = new Random();
            return getRegions().get(rand.nextInt(getRegions().size()));
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if ((Teams.getTeamByPlayer(p).isPresent() && Teams.getTeamByPlayer(p).get().isObserver()) || !GameHandler.getGameHandler().getMatch().isRunning()) {
            for (RegionModule region : getRegions()) {
                if (region.contains(event.getTo()) && currentRegion.equals(region)) {
                    if (pickupFilter == null || (pickupFilter != null && pickupFilter.evaluate(event.getPlayer()).equals(FilterState.ALLOW))) {
                        Flag flag = Flags.getFlag(this);
                        if (flag != null) {
                            Bukkit.broadcastMessage(p.getName() + " picked up " + flag.getName());
                            flag.setPicker(p);
                            PlayerPickupFlagEvent e = new PlayerPickupFlagEvent(p, flag);
                            Bukkit.getServer().getPluginManager().callEvent(e);
                        }
                    }
                }
            }
        }
    }
}
