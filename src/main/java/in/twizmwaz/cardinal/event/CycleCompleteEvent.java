package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.match.Match;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by kevin on 11/19/14.
 */
public class CycleCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Match match;

    public CycleCompleteEvent(Match match) {
        this.match = match;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Match getMatch() {
        return match;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
