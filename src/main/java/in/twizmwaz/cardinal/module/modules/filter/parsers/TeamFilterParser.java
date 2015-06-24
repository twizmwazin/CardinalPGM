package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.jdom2.Element;

public class TeamFilterParser extends FilterParser {

    private final TeamModule team;

    public TeamFilterParser(final Element element) {
        super(element);
        this.team = TeamUtils.getTeamById(element.getText()).orNull();
    }

    public TeamModule getTeam() {
        return this.team;
    }

}
