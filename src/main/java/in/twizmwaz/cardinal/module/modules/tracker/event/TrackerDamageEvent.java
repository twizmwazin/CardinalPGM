package in.twizmwaz.cardinal.module.modules.tracker.event;

import in.twizmwaz.cardinal.module.modules.tracker.DamageTracker;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class TrackerDamageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final OfflinePlayer damager;
    private final ItemStack damagerItem;
    private final DamageTracker.Type damageType;
    private DamageTracker.SpecificType specificType = null;

    private Location initialLocation;

    public TrackerDamageEvent(Player player, OfflinePlayer damager, ItemStack damagerItem, DamageTracker.Type damageType) {
        this.player = player;
        this.damager = damager;
        this.damagerItem = damagerItem;
        this.damageType = damageType;

        if (damager != null && damager.isOnline()) initialLocation = ((Player) damager).getLocation();
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

    public DamageTracker.Type getDamageType() {
        return damageType;
    }

    public ItemStack getDamagerItem() {
        return damagerItem;
    }

    public DamageTracker.SpecificType getSpecificType() {
        return specificType;
    }

    public boolean hasSpecificType() {
        return specificType != null;
    }

    public void setSpecificType(DamageTracker.SpecificType specificType) {
        this.specificType = specificType;
    }

    public Location getInitialLocation() {
        return initialLocation;
    }
}
