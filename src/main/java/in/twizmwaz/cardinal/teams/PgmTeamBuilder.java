package in.twizmwaz.cardinal.teams;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.match.Match;
import org.apache.logging.log4j.core.helpers.Integers;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 11/18/14.
 */
public class PgmTeamBuilder implements Runnable {

    private Match match;
    private Scoreboard scoreboard;
    private List<PgmTeam> teams;

    public PgmTeamBuilder(Match match, Scoreboard scoreboard) {
        this.match = match;
        this.scoreboard = scoreboard;
        teams = new ArrayList<PgmTeam>();
    }

    @Override
    public void run() {
        Document doc = match.getDocument();
        Element teams = doc.getRootElement().getChild("teams");
        List<Element> teamElements = teams.getChildren();
        for (Element teamNode : teamElements) {
            String name = teamNode.getText();
            String id;
            try {
                id = teamNode.getAttribute("id").getValue();
            } catch (NullPointerException ex) {
                id = name;
            }
            int max = Integers.parseInt(teamNode.getAttribute("max").getValue());
            int maxOverfill;
            try {
                maxOverfill = Integers.parseInt(teamNode.getAttribute("max-overfill").getValue(), (int) 1.2 * max);
            } catch (NullPointerException ex) {
                maxOverfill = (int) 1.2 * max;
            }
            int respawnLimit;
            try {
                respawnLimit = Integers.parseInt(teamNode.getAttribute("respawn-limit").getValue(), -1);
            } catch (NullPointerException ex) {
                respawnLimit = -1;
            }
            ChatColor color = StringUtils.convertStringToChatColor(teamNode.getAttribute("color").getValue());
            this.teams.add(new PgmTeam(name, id, max, maxOverfill, respawnLimit, color));

        }
        this.teams.add(new PgmObservers(this.match));

    }

    public List<PgmTeam> getTeams() {
        return teams;
    }

}
