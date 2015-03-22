package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamNameChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private TeamModule team;
    private boolean cancelled;

    public TeamNameChangeEvent(TeamModule team) {
        this.team = team;
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

    public TeamModule getTeam() {
        return team;
    }

}
