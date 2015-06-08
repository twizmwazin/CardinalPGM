package in.twizmwaz.cardinal.module.modules.playable;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Playable implements Module {

    private final RegionModule region;

    public Playable(RegionModule region) {
        this.region = region;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning() && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver()) {
            if (region != null) {
                if (region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector())) {
                    ChatUtils.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_PLAYABLE_LEAVE));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning() && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver()) {
            if (region != null) {
                if (region.contains(event.getBlock().getLocation())) {
                    ChatUtils.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_PLAYABLE_INTERACT));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning() && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver()) {
            if (region != null) {
                if (region.contains(event.getBlock().getLocation())) {
                    ChatUtils.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_PLAYABLE_INTERACT));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning() && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver()) {
            if (region != null && event.getClickedBlock() != null) {
                if (region.contains(event.getClickedBlock().getLocation())) {
                    ChatUtils.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_PLAYABLE_INTERACT));
                    event.setCancelled(true);
                }
            }
        }
    }


}
