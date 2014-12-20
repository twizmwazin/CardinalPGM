package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.GameObjective;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by kevin on 12/12/14.
 * To be called for wools, cores, monuments.
 */
public class ObjectiveCompleteEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private GameObjective objective;

    public ObjectiveCompleteEvent(GameObjective objective) {
        this.objective = objective;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public GameObjective getObjective() {
        return objective;
    }
}
