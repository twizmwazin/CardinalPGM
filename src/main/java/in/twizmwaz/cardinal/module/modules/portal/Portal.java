package in.twizmwaz.cardinal.module.modules.portal;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

public class Portal implements Module {

    private final Pair<Boolean, Double> x, y, z, yaw, pitch;
    private final RegionModule region;
    private final FilterModule filter;
    private final boolean sound, protect, bidirectional;
    private final RegionModule destination;

    protected Portal(final Pair<Boolean, Double> x,
                     final Pair<Boolean, Double> y,
                     final Pair<Boolean, Double> z,
                     final Pair<Boolean, Double> yaw,
                     final Pair<Boolean, Double> pitch,
                     final RegionModule region, final FilterModule filter, final boolean sound,
                     final boolean protect, final boolean bidirectional, final RegionModule destination) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.region = region;
        this.filter = filter;
        this.sound = sound;
        this.protect = protect;
        this.bidirectional = bidirectional;
        this.destination = destination;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector())) {
            tryTeleport(event.getPlayer(), event.getTo().clone(), destination, 1);
        }
        if (this.bidirectional && destination != null && destination.contains(event.getTo().toVector()) && !destination.contains(event.getFrom().toVector())) {
            tryTeleport(event.getPlayer(), event.getTo().clone(), region, -1);
        }
    }

    private void tryTeleport(Player player, Location from, RegionModule destination, int dir) {
        if ((filter == null || filter.evaluate(player).equals(FilterState.ALLOW)) || ObserverModule.testObserver(player)) {
            if (destination != null) {
                from.set(destination.getRandomPoint().getLocation().toVector());
            } else {
                from.setX(x.getLeft() ? from.getX() + (x.getRight() * dir) : x.getRight());
                from.setY(y.getLeft() ? from.getY() + (y.getRight() * dir) : y.getRight());
                from.setZ(z.getLeft() ? from.getZ() + (z.getRight() * dir) : z.getRight());
            }
            from.setYaw((float) (yaw.getLeft() ? from.getYaw() + (yaw.getRight() * dir) : yaw.getRight()));
            from.setPitch((float) (pitch.getLeft() ? from.getPitch() + (pitch.getRight() * dir) : pitch.getRight()));
            player.setFallDistance(0);
            player.teleport(from);
            if (sound) player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.2F, 1);
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
