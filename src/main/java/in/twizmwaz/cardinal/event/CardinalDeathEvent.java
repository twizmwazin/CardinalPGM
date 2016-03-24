package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class CardinalDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Player killer;
    private EntityDamageEvent.DamageCause cause;

    private TrackerDamageEvent trackerDamageEvent;

    public CardinalDeathEvent(Player player, Player killer, EntityDamageEvent.DamageCause cause) {
        this.player = player;
        this.killer = killer;
        this.cause = cause;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getKiller() {
        return killer;
    }

    public void setKiller(Player killer) {
        this.killer = killer;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return cause;
    }

    public TrackerDamageEvent getTrackerDamageEvent() {
        return trackerDamageEvent;
    }

    public void setTrackerDamageEvent(TrackerDamageEvent trackerDamageEvent) {
        this.trackerDamageEvent = trackerDamageEvent;
    }

}
