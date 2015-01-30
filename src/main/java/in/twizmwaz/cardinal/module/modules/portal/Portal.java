package in.twizmwaz.cardinal.module.modules.portal;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import org.bukkit.Location;
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

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (region.contains(new BlockRegion(null, event.getTo().toVector()))) {
            if (destination != null) {
                event.setTo(destination.getRandomPoint().getLocation());
            } else {
                Location newLocation = event.getTo();
                if (xRelative) newLocation.setX(newLocation.getX() + location.getX()); else newLocation.setX(location.getX());
                if (yRelative) newLocation.setY(newLocation.getY() + location.getY()); else newLocation.setY(location.getY());
                if (zRelative) newLocation.setZ(newLocation.getZ() + location.getZ()); else newLocation.setZ(location.getZ());
                if (yawRelative) newLocation.setYaw(newLocation.getYaw() + yaw); else newLocation.setYaw(yaw);
                if (pitchRelative) newLocation.setPitch(newLocation.getPitch() + pitch); else newLocation.setPitch(pitch);
                event.setTo(newLocation);
            }
        }
        if (this.bidirectional && destination != null && destination.contains(new BlockRegion(null, event.getTo().toVector()))) {
            event.setTo(region.getRandomPoint().getLocation());
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
