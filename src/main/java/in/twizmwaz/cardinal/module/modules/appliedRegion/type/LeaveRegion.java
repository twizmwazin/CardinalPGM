package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class LeaveRegion extends AppliedRegion {

    public LeaveRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (ObserverModule.testObserverOrDead(event.getPlayer())) return;
        if (!region.contains(event.getTo().toVector())
                && region.contains(event.getFrom().toVector())
                && filter.evaluate(event.getPlayer(), event).equals(FilterState.DENY)) {
            event.setTo(event.getFrom());
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }
}