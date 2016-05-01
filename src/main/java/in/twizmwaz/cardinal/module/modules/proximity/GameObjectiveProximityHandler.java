package in.twizmwaz.cardinal.module.modules.proximity;

import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveProximityEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
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

public class GameObjectiveProximityHandler implements Module {

    private GameObjective objective = null;
    private Vector location;
    private Boolean horizontal;
    private Boolean needsTouch;
    private ProximityMetric metric;

    private Boolean active;

    private Double proximity;

    public GameObjectiveProximityHandler(Vector location, boolean horizontal, boolean needsTouch, ProximityMetric metric) {
        this.location = location;
        this.horizontal = horizontal;
        this.needsTouch = needsTouch;
        this.metric = metric;
        this.proximity = Double.POSITIVE_INFINITY;
        if (horizontal) this.location.setY(0);

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
        return needsTouch ? metric.getTouchedName() : metric.getName();
    }

    private void setProximity(Location loc, Player player) {
        if (location == null) return;
        if (horizontal) {
            loc = loc.clone();
            loc.setY(0);
        }
        Double newProx = loc.distance(location);
        if (newProx < proximity) {
            Double old = proximity;
            proximity = newProx;
            Bukkit.getServer().getPluginManager().callEvent(new ObjectiveProximityEvent(objective, player, old, proximity));
        }
    }

    private void tryUpdate(Player player, Block block) {
        TeamModule team = Teams.getTeamByPlayer(player).get();
        if ((Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get().isObserver())) return;
        if (objective instanceof WoolObjective) {
            if (team.equals(objective.getTeam())) {
                boolean update = !needsTouch;
                if (needsTouch) {
                    if (metric.equals(ProximityMetric.CLOSEST_BLOCK)) {
                        if (block.getType().equals(Material.WOOL) && ((Wool) block.getState().getData()).getColor().equals(((WoolObjective) objective).getColor())) update = true;
                    } else {
                        ItemStack item = new ItemStack(Material.WOOL, 1, ((WoolObjective) objective).getColor().getWoolData());
                        if (player.getInventory().containsAtLeast(item, 1)) update = true;
                    }
                }
                if (update) setProximity(player.getLocation(), player);
            }
        } else {
            if (!team.equals(objective.getTeam())) {
                setProximity(player.getLocation(), player);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (!active || !metric.equals(ProximityMetric.CLOSEST_PLAYER)) return;
        tryUpdate(event.getPlayer(), null);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (!active || !metric.equals(ProximityMetric.CLOSEST_BLOCK)) return;
        tryUpdate(event.getPlayer(), event.getBlockPlaced());
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKill(CardinalDeathEvent event) {
        if (!active || !metric.equals(ProximityMetric.CLOSEST_KILL) || event.getKiller() == null
                || Teams.getTeamByPlayer(event.getKiller()).orNull() == Teams.getTeamByPlayer(event.getPlayer()).orNull()) return;
        tryUpdate(event.getKiller(), null);
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        this.active = !needsTouch;
    }

    @EventHandler
    public void onTouchEvent(ObjectiveTouchEvent event) {
        if (event.getObjective().equals(objective)) this.active = needsTouch;
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        if (event.getObjective().equals(objective)) this.active = false;
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
