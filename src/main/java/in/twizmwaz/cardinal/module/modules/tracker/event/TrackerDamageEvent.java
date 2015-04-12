package in.twizmwaz.cardinal.module.modules.tracker.event;

import in.twizmwaz.cardinal.module.modules.tracker.Cause;
import in.twizmwaz.cardinal.module.modules.tracker.Description;
import in.twizmwaz.cardinal.module.modules.tracker.Type;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class TrackerDamageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final OfflinePlayer damager;
    private final ItemStack item;
    private final Cause cause;
    private final Description description;
    private final Type type;
    private final int distance;
    private final long time;

    public TrackerDamageEvent(Player player, OfflinePlayer damager, ItemStack item, Cause cause, Description description, Type type) {
        this.player = player;
        this.damager = damager;
        this.item = item;
        this.cause = cause;
        this.description = description;
        this.type = type;
        this.time = System.currentTimeMillis();

        if (damager instanceof Player) {
            this.distance = (int) Math.round(player.getLocation().distance(((Player) damager).getLocation()));
        } else {
            this.distance = -1;
        }
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public OfflinePlayer getDamager() {
        return damager;
    }

    public Type getType() {
        return type;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getDistance() {
        return distance;
    }

    public Cause getCause() {
        return cause;
    }

    public Description getDescription() {
        return description;
    }

    public long getTime() {
        return time;
    }
}
