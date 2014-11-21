package in.twizmwaz.cardinal.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by kevin on 11/19/14.
 */
public class MatchStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
