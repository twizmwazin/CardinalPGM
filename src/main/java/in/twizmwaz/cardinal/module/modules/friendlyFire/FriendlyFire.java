package in.twizmwaz.cardinal.module.modules.friendlyFire;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.event.HandlerList;

public class FriendlyFire implements Module {

    private Match match;
    //unimplemented
    private boolean arrowReturn;

    protected FriendlyFire(Match match, boolean enabled, boolean arrowReturn) {
        this.match = match;
        this.arrowReturn = arrowReturn;
        if (enabled) {
            for (PgmTeam team : match.getTeams()) {
                team.getScoreboardTeam().setAllowFriendlyFire(false);
            }
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
