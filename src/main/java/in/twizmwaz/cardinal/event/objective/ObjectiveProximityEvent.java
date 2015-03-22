package in.twizmwaz.cardinal.event.objective;

import in.twizmwaz.cardinal.module.GameObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ObjectiveProximityEvent extends ObjectiveEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private double oldProximity;
    private double newProximity;
    private boolean cancelled;

    public ObjectiveProximityEvent(GameObjective objective, Player player, double oldProximity, double newProximity) {
        super(objective, player);
        this.oldProximity = oldProximity;
        this.newProximity = newProximity;
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

    public double getOldProximity() {
        return oldProximity;
    }

    public double getNewProximity() {
        return newProximity;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
