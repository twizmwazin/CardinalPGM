package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class UseRegion extends AppliedRegion {

    public UseRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (region.contains(new BlockRegion(null, event.getClickedBlock().getLocation().toVector())) && filter.evaluate(event.getPlayer(), event.getClickedBlock(), event).equals(FilterState.DENY)) {
            event.setUseItemInHand(Event.Result.ALLOW);
            event.setUseInteractedBlock(Event.Result.DENY);
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }

}
