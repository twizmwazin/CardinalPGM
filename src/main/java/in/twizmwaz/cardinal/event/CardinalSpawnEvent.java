package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CardinalSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final SpawnModule spawn;
    private final TeamModule team;
    private boolean cancelled;

    public CardinalSpawnEvent(final Player player, final SpawnModule spawn, final TeamModule team) {
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

    public SpawnModule getSpawn() {
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
