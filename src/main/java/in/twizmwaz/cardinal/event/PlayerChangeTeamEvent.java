package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeTeamEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final boolean forced;
    private TeamModule newTeam;
    private TeamModule oldTeam;
    private boolean cancelled;

    public PlayerChangeTeamEvent(Player player, boolean forced, TeamModule newTeam, TeamModule oldTeam) {
        this.player = player;
        this.forced = forced;
        this.newTeam = newTeam;
        this.oldTeam = oldTeam;
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

    public TeamModule getNewTeam() {
        return newTeam;
    }

    public TeamModule getOldTeam() {
        return oldTeam;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }
}
