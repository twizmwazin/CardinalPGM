package in.twizmwaz.cardinal.event;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
public class MatchEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Optional<TeamModule> team;
    private final Optional<Player> player;

    public MatchEndEvent(Optional<TeamModule> team) {
        this.team = team;
        this.player = Optional.absent();
    }

    public MatchEndEvent(Player player) {
        this.team = Optional.absent();
        if (player == null) this.player = Optional.absent();
        else this.player = Optional.of(player);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Optional<TeamModule> getTeam() {
        return team;
    }

    public Optional<Player> getPlayer() {
        return player;
    }
}
