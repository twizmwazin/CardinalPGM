package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.jdom2.Element;

public class ObjectiveFilterParser extends FilterParser {

    private GameObjective objective;
    private TeamModule team;
    private boolean any;

    public ObjectiveFilterParser(final Element element) {
        super(element);
        String name = element.getText();
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective.getName().equalsIgnoreCase(name)) this.objective = objective;
        }
        try {
            String team = element.getAttributeValue("team");
            this.team = TeamUtils.getTeamById(team);
        } catch (NullPointerException e) {
        }
        try {
            this.any = Boolean.getBoolean(element.getAttributeValue("any"));
        } catch (NullPointerException e) {
            any = false;
        }

    }

    public GameObjective getObjective() {
        return objective;
    }

    public TeamModule getTeam() {
        return team;
    }

    public boolean isAny() {
        return any;
    }
}
