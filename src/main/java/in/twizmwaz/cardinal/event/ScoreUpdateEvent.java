package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScoreUpdateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private ScoreModule scoreModule;
    private boolean cancelled;

    public ScoreUpdateEvent(ScoreModule scoreModule) {
        this.scoreModule = scoreModule;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ScoreModule getScoreModule() {
        return scoreModule;
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
