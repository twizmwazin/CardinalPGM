package in.twizmwaz.cardinal.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimeLimitChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public TimeLimitChangeEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
