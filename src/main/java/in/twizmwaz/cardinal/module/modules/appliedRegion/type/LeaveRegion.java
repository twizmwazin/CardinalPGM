package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.titleRespawn.TitleRespawn;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class LeaveRegion extends AppliedRegion {

    public LeaveRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!region.contains(event.getTo().toVector())
                && region.contains(event.getFrom().toVector())
                && filter.evaluate(event.getPlayer(), event).equals(FilterState.DENY)
                && (!Teams.getTeamByPlayer(event.getPlayer()).isPresent() || !Teams.getTeamByPlayer(event.getPlayer()).get().isObserver())
                && GameHandler.getGameHandler().getMatch().isRunning()
                && !GameHandler.getGameHandler().getMatch().getModules().getModule(TitleRespawn.class).isDeadUUID(event.getPlayer().getUniqueId())) {
            event.setTo(event.getFrom());
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }
}