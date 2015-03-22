package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerSpleefEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CardinalDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Player killer;

    private TrackerDamageEvent trackerDamageEvent;
    private TrackerSpleefEvent trackerSpleefEvent;

    private Location initialLocation;

    public CardinalDeathEvent(Player player, Player killer) {
        this.player = player;
        this.killer = killer;
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

    public TrackerDamageEvent getTrackerDamageEvent() {
        return trackerDamageEvent;
    }

    public void setTrackerDamageEvent(TrackerDamageEvent trackerDamageEvent) {
        this.trackerDamageEvent = trackerDamageEvent;
        this.initialLocation = trackerDamageEvent.getInitialLocation();
    }

    public TrackerSpleefEvent getTrackerSpleefEvent() {
        return trackerSpleefEvent;
    }

    public void setPlayerSpleefEvent(TrackerSpleefEvent trackerSpleefEvent) {
        this.trackerSpleefEvent = trackerSpleefEvent;
        this.initialLocation = trackerSpleefEvent.getInitialLocation();
    }

    public boolean hasTrackerDamageEvent() {
        return trackerDamageEvent != null;
    }

    public boolean hasTrackerSpleefEvent() {
        return trackerSpleefEvent != null;
    }
}
