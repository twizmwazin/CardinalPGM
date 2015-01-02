package in.twizmwaz.cardinal.filter.parsers;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.jdom2.Element;

public class TeamFilterParser {

    private final PgmTeam team;

    public TeamFilterParser(final Element element) {
        this.team = GameHandler.getGameHandler().getMatch().getTeamById(element.getText());
    }

    public PgmTeam getTeam() {
        return this.team;
    }

}
