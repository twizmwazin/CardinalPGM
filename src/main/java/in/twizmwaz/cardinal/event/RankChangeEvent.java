package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.rank.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RankChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private Rank rank;
    private boolean adding;
    private boolean online;

    public RankChangeEvent(Player player, Rank rank, boolean adding, boolean online) {
        this.player = player;
        this.rank = rank;
        this.adding = adding;
        this.online = online;
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

    public Rank getRank() {
        return rank;
    }

    public boolean isAdding() {
        return adding;
    }

    public boolean isOnline() {
        return online;
    }

}
