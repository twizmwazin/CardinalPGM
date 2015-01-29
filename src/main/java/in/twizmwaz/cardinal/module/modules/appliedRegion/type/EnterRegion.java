package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class EnterRegion extends AppliedRegion {
    
    public EnterRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (region.contains(new BlockRegion(null, event.getTo().toVector())) && !region.contains(new BlockRegion(null, event.getFrom().toVector())) && filter.evaluate(event.getPlayer()).equals(FilterState.DENY)
                && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() && GameHandler.getGameHandler().getMatch().isRunning()) {
            event.setTo(event.getFrom());
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }
}
