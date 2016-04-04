package in.twizmwaz.cardinal.event.objective;

import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ObjectiveUncompleteEvent extends ObjectiveEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private TeamModule oldTeam;

    public ObjectiveUncompleteEvent(GameObjective objective, TeamModule oldTeam) {
        super(objective, null);
        this.oldTeam = oldTeam;
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

    public TeamModule getOldTeam() {
        return oldTeam;
    }

}
