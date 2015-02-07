package in.twizmwaz.cardinal.module.modules.portal;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class Portal implements Module {

    private final Vector location;
    private final boolean xRelative, yRelative, zRelative;
    private final RegionModule region;
    private final FilterModule filter;
    private final boolean sound, protect, bidirectional;
    private final int yaw, pitch;
    private final boolean yawRelative, pitchRelative;
    private final RegionModule destination;

    protected Portal(final Vector location, final boolean xRelative, final boolean yRelative, final boolean zRelative, final RegionModule region, final FilterModule filter, final boolean sound, final boolean protect, final boolean bidirectional, final int yaw, final boolean yawRelative, final int pitch, final boolean pitchRelative, final RegionModule destination) {
        this.location = location;
        this.xRelative = xRelative;
        this.yRelative = yRelative;
        this.zRelative = zRelative;
        this.region = region;
        this.filter = filter;
        this.sound = sound;
        this.protect = protect;
        this.bidirectional = bidirectional;
        this.yaw = yaw;
        this.yawRelative = yawRelative;
        this.pitch = pitch;
        this.pitchRelative = pitchRelative;
        this.destination = destination;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (region.contains(new BlockRegion(null, event.getTo().toVector()))) {
            if ((filter == null || filter.evaluate(event.getPlayer()).equals(FilterState.ALLOW)) || (TeamUtils.getTeamByPlayer(event.getPlayer()) != null && TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver()) || !GameHandler.getGameHandler().getMatch().isRunning()) {
                if (destination != null) {
                    event.getPlayer().teleport(destination.getRandomPoint().getLocation());
                    if (sound) event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 0.2F, 1);
                } else {
                    Location newLocation = event.getTo();
                    if (xRelative)
                        newLocation.setX(newLocation.getX() + location.getX());
                    else
                        newLocation.setX(location.getX());
                    if (yRelative)
                        newLocation.setY(newLocation.getY() + location.getY());
                    else
                        newLocation.setY(location.getY());
                    if (zRelative)
                        newLocation.setZ(newLocation.getZ() + location.getZ());
                    else
                        newLocation.setZ(location.getZ());
                    if (yawRelative)
                        newLocation.setYaw(newLocation.getYaw() + yaw);
                    else
                        newLocation.setYaw(yaw);
                    if (pitchRelative)
                        newLocation.setPitch(newLocation.getPitch() + pitch);
                    else
                        newLocation.setPitch(pitch);
                    event.getPlayer().teleport(newLocation);
                    if (sound) event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 0.2F, 1);
                }
            }
        }
        if (destination != null && destination.contains(new BlockRegion(null, event.getTo().toVector())) && this.bidirectional) {
            if (filter == null || filter.evaluate(event.getPlayer()).equals(FilterState.ALLOW) || (TeamUtils.getTeamByPlayer(event.getPlayer()) != null && TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver()) || !GameHandler.getGameHandler().getMatch().isRunning()) {
                event.getPlayer().teleport(region.getRandomPoint().getLocation());
                if (sound) event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 0.2F, 1);
            }

        }
    }

    public RegionModule getDestination() {
        return destination;
    }

    public boolean protect() {
        return protect;
    }

    public RegionModule getRegion() {
        return region;
    }

}
