package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.kit.KitNode;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.titleRespawn.TitleRespawn;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class KitRegion extends AppliedRegion {

    private final KitNode kit;
    private boolean lend;

    public KitRegion(RegionModule region, FilterModule filter, String message, KitNode kit, boolean lend) {
        super(region, filter, message);
        this.kit = kit;
        this.lend = lend;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (ObserverModule.testObserverOrDead(event.getPlayer())) return;
        if (region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector()) && filter.evaluate(event.getPlayer(), event.getTo(), event).equals(FilterState.ALLOW)) {
            kit.apply(event.getPlayer(), null);
        } else if (lend && region.contains(event.getFrom().toVector()) && !region.contains(event.getTo().toVector())) {
            kit.remove(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerTeleportEvent event) {
        if (ObserverModule.testObserverOrDead(event.getPlayer())) return;
        if (region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector()) && filter.evaluate(event.getPlayer(), event.getTo(), event).equals(FilterState.ALLOW)) {
            kit.apply(event.getPlayer(), null);
        } else if (lend && region.contains(event.getFrom().toVector()) && !region.contains(event.getTo().toVector())) {
            kit.remove(event.getPlayer());
        }
    }
}
