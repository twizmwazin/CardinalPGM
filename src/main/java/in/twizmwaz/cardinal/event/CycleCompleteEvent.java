package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.match.Match;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by kevin on 11/19/14.
 */
public class CycleCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private World world;
    private Match match;



    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
