package in.twizmwaz.cardinal.event;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RankChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Optional<TeamModule> team;

    public RankChangeEvent(Player player, Optional<TeamModule> team) {
        this.player = player;
        this.team = team;
    }

    public RankChangeEvent(Player player) {
        this.player = player;
        this.team = TeamUtils.getTeamByPlayer(player);
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

    public Optional<TeamModule> getTeam() {
        return team;
    }
}
