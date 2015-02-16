package in.twizmwaz.cardinal.module.modules.proximityAlarm;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.CircleRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.CylinderRegion;
import in.twizmwaz.cardinal.util.FireworkUtil;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Team;

import java.util.Random;
import java.util.logging.Level;

public class ProximityAlarm implements Module {

    private final String message;
    private final int flareRadius;
    private final RegionModule region;
    private final FilterModule detect;
    private final FilterModule notify;

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    protected ProximityAlarm(final String message, final int flareRadius, final RegionModule region, final FilterModule detect, final FilterModule notify) {
        this.message = message;
        this.flareRadius = flareRadius;
        this.region = region;
        this.detect = detect;
        this.notify = notify;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (region.contains(new BlockRegion(null, event.getTo().toVector())) && !region.contains(new BlockRegion(null, event.getFrom().toVector())) && detect.evaluate(event.getPlayer()).equals(FilterState.ALLOW) && TeamUtils.getTeamByPlayer(event.getPlayer()) != null && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() && GameHandler.getGameHandler().getMatch().isRunning()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (notify.evaluate(player).equals(FilterState.ALLOW)) {
                    player.sendMessage(ChatColor.RED + message);
                    RegionModule radius = new CylinderRegion("radius", region.getCenterBlock().getVector(), flareRadius, 1);
                    int flareAmount = new Random().nextInt(6);
                    for (int f = 0; flareAmount > f; f++) {
                        FireworkUtil.spawnFirework(radius.getRandomPoint().getLocation(), event.getPlayer().getWorld());
                    }
                }
            }
        }
    }

}
