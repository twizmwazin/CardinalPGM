package in.twizmwaz.cardinal.module.modules.portal;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
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
        if (region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector())) {
            Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
            if ((filter == null || filter.evaluate(event.getPlayer()).equals(FilterState.ALLOW)) || (team.isPresent() && team.get().isObserver()) || !GameHandler.getGameHandler().getMatch().isRunning()) {
                if (destination != null) {
                    event.getPlayer().teleport(destination.getRandomPoint().getLocation());
                    if (sound)
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.2F, 1);
                } else {
                    Location newLocation = event.getTo();
                    newLocation.setX(location.getX() + (xRelative ? newLocation.getX() : 0));
                    newLocation.setY(location.getY() + (yRelative ? newLocation.getY() : 0));
                    newLocation.setZ(location.getZ() + (zRelative ? newLocation.getZ() : 0));
                    newLocation.setYaw(yaw + (yawRelative ? newLocation.getYaw() : 0));
                    newLocation.setPitch(pitch + (pitchRelative ? newLocation.getPitch() : 0));
                    event.getPlayer().teleport(newLocation);
                    if (sound)
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.2F, 1);
                }
            }
        }
        if (destination != null && destination.contains(event.getTo().toVector()) && !destination.contains(event.getFrom().toVector()) && this.bidirectional) {
            Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
            if (filter == null || filter.evaluate(event.getPlayer()).equals(FilterState.ALLOW) || (team.isPresent() && team.get().isObserver()) || !GameHandler.getGameHandler().getMatch().isRunning()) {
                event.getPlayer().teleport(region.getRandomPoint().getLocation());
                if (sound)
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.2F, 1);
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
