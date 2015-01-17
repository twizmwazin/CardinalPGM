package in.twizmwaz.cardinal.filter.parsers;

import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtil;
import org.jdom2.Element;

public class TeamFilterParser {

    private final TeamModule team;

    public TeamFilterParser(final Element element) {
        this.team = TeamUtil.getTeamById(element.getText());
    }

    public TeamModule getTeam() {
        return this.team;
    }

}
