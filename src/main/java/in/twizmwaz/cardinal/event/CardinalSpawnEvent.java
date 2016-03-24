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
    private SpawnModule spawn;
    private TeamModule team;
    private boolean cancelled;

    public CardinalSpawnEvent(final Player player, final TeamModule team) {
        this.player = player;
        this.spawn = null;
        this.team = team;
    }

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

    public void setSpawn(SpawnModule spawn) {
        this.spawn = spawn;
    }

    public TeamModule getTeam() {
        return team;
    }

    public void setTeam(TeamModule team) {
        this.team = team;
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
