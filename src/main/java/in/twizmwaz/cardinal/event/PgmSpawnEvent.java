package in.twizmwaz.cardinal.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by kevin on 11/22/14.
 */
public class PgmSpawnEvent extends Event implements Cancellable {

    private final Player player;
    private boolean cancelled;

    public PgmSpawnEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
