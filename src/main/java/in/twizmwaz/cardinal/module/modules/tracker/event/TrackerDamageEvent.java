package in.twizmwaz.cardinal.module.modules.tracker.event;

import in.twizmwaz.cardinal.module.modules.tracker.DamageTracker;
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

    public TrackerDamageEvent(Player player, OfflinePlayer damager, ItemStack damagerItem, DamageTracker.Type damageType) {
        this.player = player;
        this.damager = damager;
        this.damagerItem = damagerItem;
        this.damageType = damageType;
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
}
