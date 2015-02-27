package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerNameUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final TeamModule team;

    public PlayerNameUpdateEvent(Player player, TeamModule team) {
        this.player = player;
        this.team = team;
    }

    public PlayerNameUpdateEvent(Player player) {
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

    public TeamModule getTeam() {
        return team;
    }
}
