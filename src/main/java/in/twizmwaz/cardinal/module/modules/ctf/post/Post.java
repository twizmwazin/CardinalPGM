package in.twizmwaz.cardinal.module.modules.ctf.post;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.flag.FlagPickupEvent;
import in.twizmwaz.cardinal.event.flag.FlagRespawnEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.material.Banner;

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
    private BlockFace yaw;

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
        this.currentBlock = getInitialBlock();
        this.yaw = yaw == Float.MIN_VALUE ? ((Banner)currentBlock.getState().getMaterialData()).getFacing() : Flags.yawToFace(yaw);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public String getId() {
        return id;
    }

    public TeamModule getOwner() {
        return owner;
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

    public Block getCurrentBlock() {
        return this.currentBlock;
    }

    public int getRespawnTime(Location loc1, Location loc2) {
        return respawnTime == -1 ? (int)Math.round(loc1.distance(loc2)/respawnSpeed) : respawnTime;
    }

    public int getRecoverTime() {
        return recoverTime;
    }

    public BlockFace getYaw() {
        return yaw;
    }

    public int getPointsRate() {
        return pointsRate;
    }

    public void tryPickupFlag(Player player, Location to, Location from, FlagObjective flag) {
        if (!GameHandler.getGameHandler().getMatch().isRunning() || permanent || flag == null || flag.isCarried() || flag.isRespawning() || Flags.getFlag(player) != null) return;

        TeamModule team = Teams.getTeamOrPlayerByPlayer(player).orNull();
        if (team == null || team.isObserver()) return;
        if ((flag.isShared() || (flag.getTeam() != null && !flag.getTeam().equals(team))) && flag.inRange(to, from)) {
            FilterModule pickupFilt = null;
            if (getPickupFilter() != null || flag.getPickupFilter() != null) {
                pickupFilt = getPickupFilter() != null ? getPickupFilter() : flag.getPickupFilter();
            }
            if (pickupFilt == null || pickupFilt.evaluate(player).equals(FilterState.ALLOW)) {
                FlagPickupEvent e = new FlagPickupEvent(player, flag);
                Bukkit.getServer().getPluginManager().callEvent(e);
            }
        }
    }

    @EventHandler
    public void onFlagRespawn(FlagRespawnEvent event) {
        if (event.getPost().equals(this)) {
            setCurrentBlock(event.getBlock());
        }
    }
}
