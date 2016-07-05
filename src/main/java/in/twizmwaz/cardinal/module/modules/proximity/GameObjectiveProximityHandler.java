package in.twizmwaz.cardinal.module.modules.proximity;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.flag.FlagCaptureEvent;
import in.twizmwaz.cardinal.event.flag.FlagPickupEvent;
import in.twizmwaz.cardinal.event.flag.FlagRespawnEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveProximityEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class GameObjectiveProximityHandler implements Module {

    private GameObjective objective = null;
    private TeamModule team;
    private ProximityInfo info;

    private Boolean active;

    private Double proximity;

    public GameObjectiveProximityHandler(TeamModule team, ProximityInfo info) {
        this.team = team;
        this.info = info;
        this.proximity = Double.POSITIVE_INFINITY;

        active = false;
    }

    public void setObjective(GameObjective objective) {
        this.objective = objective;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
    
    public GameObjective getObjective() {
        return objective;
    }
    
    public Double getProximity() {
        return proximity;
    }

    public String getProximityAsString() {
        return (proximity.equals(Double.POSITIVE_INFINITY) ? "Infinity" : (Math.round(proximity * 100.0) / 100.0) + "");
    }

    public String getProximityName() {
        return info.needsTouch ? info.metric.getTouchedName() : info.metric.getName();
    }

    private void setProximity(Location loc, Player player) {
        if (info.locations == null) return;
        if (info.horizontal) {
            loc = loc.clone();
            loc.setY(0);
        }
        double newProximity = proximity;
        for (Vector proxLoc : info.locations) {
            double prox = proxLoc.distance(loc);
            if (prox < newProximity) {
                newProximity = prox;
            }
        }
        if (newProximity < proximity) {
            Double old = proximity;
            proximity = newProximity;
            Bukkit.getServer().getPluginManager().callEvent(new ObjectiveProximityEvent(objective, player, old, proximity));
        }
    }

    private void tryUpdate(Player player, Block block) {
        if (!teamAllowsUpdate(Teams.getTeamByPlayer(player))) return;
        boolean update = true;
        if (objective instanceof WoolObjective) {
            update = !info.needsTouch;
            if (info.needsTouch) {
                if (info.metric.equals(ProximityMetric.CLOSEST_BLOCK)) {
                    if (block.getType().equals(Material.WOOL) && ((Wool) block.getState().getData()).getColor().equals(((WoolObjective) objective).getColor()))
                        update = true;
                } else {
                    ItemStack item = new ItemStack(Material.WOOL, 1, ((WoolObjective) objective).getColor().getWoolData());
                    if (player.getInventory().containsAtLeast(item, 1)) update = true;
                }
            }
        } else if (objective instanceof FlagObjective) {
            if (info.needsTouch) {
                update = Flags.getFlag(player) == objective;
            }
        }
        if (update) setProximity(player.getLocation(), player);
    }

    public boolean teamAllowsUpdate(Optional<TeamModule> team) {
        return !(team.isPresent() && team.get().isObserver()) && team.orNull() == this.team;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void reset() {
        this.proximity = Double.POSITIVE_INFINITY;
        setActive(false);
    }

    public void setLocation(Location location) {
        Set<Vector> locations = new HashSet<>();
        locations.add(location);
        this.info.setLocations(locations);
    }

    public void setLocations(Set<Vector> locations) {
        this.info.setLocations(locations);
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        if (objective instanceof FlagObjective && info.needsTouch) {
            Set<Net> nets = Flags.getNetsByFlag((FlagObjective) objective);
            Set<Vector> locations = new HashSet<>();
            for (Net net : nets) locations.add(net.getLocation());
            setLocations(locations);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (!active || !info.metric.equals(ProximityMetric.CLOSEST_PLAYER) && event.getFrom().getBlock() != event.getTo().getBlock()) return;
        tryUpdate(event.getPlayer(), null);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (!active || !info.metric.equals(ProximityMetric.CLOSEST_BLOCK)) return;
        tryUpdate(event.getPlayer(), event.getBlockPlaced());
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKill(CardinalDeathEvent event) {
        if (!active || !info.metric.equals(ProximityMetric.CLOSEST_KILL) || event.getKiller() == null
                || Teams.getTeamByPlayer(event.getKiller()).orNull() == Teams.getTeamByPlayer(event.getPlayer()).orNull()) return;
        tryUpdate(event.getKiller(), null);
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        this.active = !info.needsTouch;
    }

    @EventHandler
    public void onTouchEvent(ObjectiveTouchEvent event) {
        if (event.getObjective().equals(objective)) this.active = info.needsTouch;
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        if (event.getObjective().equals(objective)) this.active = false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFlagRespawn(FlagRespawnEvent event) {
        if (event.getFlag().equals(this.objective)) {
            this.active = !info.needsTouch;
            if (this.active) setLocation(event.getPost().getCurrentBlock().getLocation());
        }
    }

    @EventHandler
    public void onFlagPickUp(FlagPickupEvent event) {
        if (event.getFlag().equals(this.objective)) {
            this.active = info.needsTouch;
        }
    }

    @EventHandler
    public void onFlagCapture(FlagCaptureEvent event) {
        if (event.getFlag().equals(this.objective)) {
            this.reset();
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        this.active = false;
    }

    public enum ProximityMetric {
        CLOSEST_PLAYER("closest player", "closest player"),
        CLOSEST_BLOCK("closest block", "closest safety"),
        CLOSEST_KILL("closest kill", "closest kill"),
        NULL_PROXIMITY(null, null);

        private final String name;
        private final String touchedName;

        ProximityMetric(String name, String touchedName) {
            this.name = name;
            this.touchedName = touchedName;
        }

        public String getName() {
            return this.name;
        }

        public String getTouchedName() {
            return this.touchedName;
        }

        public static ProximityMetric getByName(String name) {
            for(ProximityMetric prox : ProximityMetric.values()) {
                if(prox.name.equals(name)) return prox;
            }
            return NULL_PROXIMITY;
        }
    }

}
