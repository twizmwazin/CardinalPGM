package in.twizmwaz.cardinal.teams;

import in.twizmwaz.cardinal.match.Match;
import org.bukkit.ChatColor;

/**
 * Created by kevin on 11/18/14.
 */
public class PgmObservers extends PgmTeam {
    PgmObservers(Match match) {
        super("Observers", "observers", Integer.MAX_VALUE, Integer.MAX_VALUE, -1, ChatColor.AQUA);
    }
}
