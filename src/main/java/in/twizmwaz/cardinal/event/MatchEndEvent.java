package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final TeamModule team;

    public MatchEndEvent(TeamModule team) {
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public TeamModule getTeam() throws NullPointerException {
        try {
            return team;
        } catch (NullPointerException ex) {
            throw new NullPointerException("No valid winning team");
        }
    }
}
