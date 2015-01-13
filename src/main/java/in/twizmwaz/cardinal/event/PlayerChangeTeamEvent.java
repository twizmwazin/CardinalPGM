package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.JoinType;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeTeamEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private PgmTeam newTeam;
    private PgmTeam oldTeam;
    private JoinType joinType;
    private boolean cancelled;

    public PlayerChangeTeamEvent(Player player, PgmTeam team, JoinType joinType) {
        this.player = player;
        this.newTeam = team;
        this.joinType = joinType;
        try {
            this.oldTeam = GameHandler.getGameHandler().getMatch().getTeam(player);
        } catch (NullPointerException ex) {
            this.oldTeam = GameHandler.getGameHandler().getMatch().getTeamById("observers");
        }
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

    public JoinType getJoinType() {
        return joinType;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }
}
