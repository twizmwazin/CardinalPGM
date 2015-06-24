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

    public MatchEndEvent(TeamModule team) {
        this.team = Optional.of(team);
        this.player = Optional.absent();
    }

    public MatchEndEvent(Player player) {
        this.team = Optional.absent();
        this.player = Optional.of(player);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Optional<TeamModule> getTeam() throws NullPointerException {
        try {
            return team;
        } catch (NullPointerException ex) {
            throw new NullPointerException("No valid winning team");
        }
    }
}
