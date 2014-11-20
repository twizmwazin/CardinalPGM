package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by kevin on 11/19/14.
 */
public class MatchEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final PgmTeam team;

    public MatchEndEvent(PgmTeam team) {
        this.team = team;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PgmTeam getTeam() {
        return team;
    }

}
