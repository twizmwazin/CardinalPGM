package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BroadcastMatchEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
