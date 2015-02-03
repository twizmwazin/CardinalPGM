package in.twizmwaz.cardinal.module.modules.proximityAlarm;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.logging.Level;

public class ProximityAlarm implements Module{

    private final String message;
    private final int flareradius;
    private final RegionModule region;
    private final FilterModule detect;
    private final FilterModule notify;

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    protected ProximityAlarm(final String message, final int flareradius, final RegionModule region, final FilterModule detect, final FilterModule notify) {
        this.message = message;
        this.flareradius = flareradius;
        this.region = region;
        this.detect = detect;
        this.notify = notify;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (region.contains(new BlockRegion(null, event.getTo().toVector())) && !region.contains(new BlockRegion(null, event.getFrom().toVector())) && !detect.evaluate(event.getPlayer()).equals(FilterState.DENY)
                && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() && GameHandler.getGameHandler().getMatch().isRunning()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (notify.evaluate(player).equals(FilterState.ALLOW)) {
                    player.sendMessage(ChatColor.RED + ChatColor.translateAlternateColorCodes('`',message));
                }
            }
        }
    }

}
