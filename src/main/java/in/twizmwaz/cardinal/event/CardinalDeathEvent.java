package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.tracker.event.PlayerSpleefEvent;
import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CardinalDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Player killer;

    private TrackerDamageEvent trackerDamageEvent;
    private PlayerSpleefEvent playerSpleefEvent;

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
    }

    public PlayerSpleefEvent getPlayerSpleefEvent() {
        return playerSpleefEvent;
    }

    public void setPlayerSpleefEvent(PlayerSpleefEvent playerSpleefEvent) {
        this.playerSpleefEvent = playerSpleefEvent;
    }
}
