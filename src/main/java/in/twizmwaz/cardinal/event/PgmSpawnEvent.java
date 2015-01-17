package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.teams.spawns.Spawn;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PgmSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Spawn spawn;
    private final TeamModule team;
    private boolean cancelled;

    public PgmSpawnEvent(final Player player, final Spawn spawn, final TeamModule team) {
        this.player = player;
        this.spawn = spawn;
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public TeamModule getTeam() {
        return team;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
