package in.twizmwaz.cardinal.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerVisibilityChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;

    public PlayerVisibilityChangeEvent(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }
}
