package in.twizmwaz.cardinal.module.modules.tracker.event;

import in.twizmwaz.cardinal.module.modules.tracker.SpleefTracker;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerSpleefEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player spleefed;
    private final OfflinePlayer spleefer;
    private final ItemStack spleeferItem;
    private final SpleefTracker.Type spleefType;

    public PlayerSpleefEvent(Player spleefed, OfflinePlayer spleefer, ItemStack spleeferItem, SpleefTracker.Type spleefType) {
        this.spleefed = spleefed;
        this.spleefer = spleefer;
        this.spleeferItem = spleeferItem;
        this.spleefType = spleefType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getSpleefed() {
        return spleefed;
    }

    public OfflinePlayer getSpleefer() {
        return spleefer;
    }

    public SpleefTracker.Type getSpleefType() {
        return spleefType;
    }

    public ItemStack getSpleeferItem() {
        return spleeferItem;
    }
}
