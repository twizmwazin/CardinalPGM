package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by kevin on 11/22/14.
 */
public class PlayerChangeTeamEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final PgmTeam newTeam;
    private final PgmTeam oldTeam;

    private boolean cancelled;

    public PlayerChangeTeamEvent(Player player, PgmTeam team) {
        this.player = player;
        this.newTeam = team;
        this.oldTeam = GameHandler.getGameHandler().getMatch().getTeam(player);
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

    public PgmTeam getNewTeam() {
        return newTeam;
    }

    public PgmTeam getOldTeam() {
        return oldTeam;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }

}
