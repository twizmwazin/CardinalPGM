package in.twizmwaz.cardinal.module.modules.friendlyFire;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameScoreboard;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.event.HandlerList;

public class FriendlyFire implements Module {

    private Match match;
    private boolean arrowReturn;

    protected FriendlyFire(Match match, boolean enabled, boolean arrowReturn) {
        this.match = match;
        this.arrowReturn = arrowReturn;
        if (enabled) {
            for (TeamModule team : TeamUtils.getTeams()) {
                for (GameScoreboard scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(GameScoreboard.class)) {
                    scoreboard.getScoreboard().getTeam(team.getId()).setAllowFriendlyFire(false);
                }
            }
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
