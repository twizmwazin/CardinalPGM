package in.twizmwaz.cardinal.module.modules.ctf.post;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.flag.FlagDropEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.event.flag.FlagRespawnEvent;
import in.twizmwaz.cardinal.event.flag.FlagPickupEvent;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
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

    private Block currentBlock;
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

        this.currentBlock = getInitialBlock();
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

    public Block getInitialBlock() {
        return getRegions().get(0).getCenterBlock().getBlock();
    }

    public void setCurrentBlock(Block block) {
        this.currentBlock = block;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public int getRecoverTime() {
        return recoverTime;
    }

    public float getYaw() {
        return yaw;
    }

    public int getPointsRate() {
        return pointsRate;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        FlagObjective flagObjective = Flags.getFlag(this);
        if (flagObjective != null) {
            if ((Teams.getTeamByPlayer(p).isPresent() && !Teams.getTeamByPlayer(p).get().isObserver()) && GameHandler.getGameHandler().getMatch().isRunning()) {
                TeamModule team = Teams.getTeamByPlayer(p).get();
                for (RegionModule region : getRegions()) {
                    if (region.contains(event.getTo()) && !region.contains(event.getFrom()) && currentBlock.equals(region.getCenterBlock().getBlock())) {
                        FilterModule pickupFilt = null;
                        if (getPickupFilter() != null || flagObjective.getPickupFilter() != null) {
                            pickupFilt = getPickupFilter() != null ? getPickupFilter() : flagObjective.getPickupFilter();
                        }
                        if (pickupFilt == null || pickupFilt.evaluate(event.getPlayer()).equals(FilterState.ALLOW)) {
                            if (!flagObjective.isCarried() && (flagObjective.isShared() || flagObjective.getTeam().equals(team))) {
                                FlagPickupEvent e = new FlagPickupEvent(p, flagObjective);
                                Bukkit.getServer().getPluginManager().callEvent(e);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFlagDrop(FlagDropEvent event) {
        setCurrentBlock(event.getFlag().getCurrentFlagBlock());
    }

    @EventHandler
    public void onFlagRespawn(FlagRespawnEvent event) {
        if (event.getPost().equals(this)) {
            setCurrentBlock(event.getBlock());
        }
    }
}
