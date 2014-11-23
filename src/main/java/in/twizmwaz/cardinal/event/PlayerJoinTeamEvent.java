package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by kevin on 11/22/14.
 */
public class PlayerJoinTeamEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final PgmTeam team;

    public PlayerJoinTeamEvent(Player player, PgmTeam team) {
        this.player = player;
        this.team = team;
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

    public PgmTeam getTeam() {
        return team;
    }

}
