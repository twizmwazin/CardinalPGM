package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class KitRegion extends AppliedRegion {

    private final Kit kit;

    public KitRegion(RegionModule region, FilterModule filter, String message, Kit kit) {
        super(region, filter, message);
        this.kit = kit;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (event.isCancelled() || (team.isPresent() && team.get().isObserver())) return;
        if (region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector()) && (filter == null || filter.evaluate(event.getPlayer(), event.getTo(), event).equals(FilterState.ALLOW))) {
            kit.apply(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerTeleportEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (event.isCancelled() || (team.isPresent() && team.get().isObserver())) return;
        if (region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector()) && (filter == null || filter.evaluate(event.getPlayer(), event.getTo(), event).equals(FilterState.ALLOW))) {
            kit.apply(event.getPlayer());
        }
    }
}
