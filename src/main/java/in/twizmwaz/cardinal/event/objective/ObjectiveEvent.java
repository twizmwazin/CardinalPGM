package in.twizmwaz.cardinal.event.objective;

import in.twizmwaz.cardinal.module.GameObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ObjectiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    protected GameObjective objective;
    protected Player player;

    public ObjectiveEvent(GameObjective objective, Player player) {
        this.objective = objective;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public GameObjective getObjective() {
        return objective;
    }

    public Player getPlayer() {
        return player;
    }
}
