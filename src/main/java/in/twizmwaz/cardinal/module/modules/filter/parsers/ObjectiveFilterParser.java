package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Teams;
import org.jdom2.Element;

public class ObjectiveFilterParser extends FilterParser {

    private Element element;
    private GameObjective objective;
    private TeamModule team;
    private boolean any;

    public ObjectiveFilterParser(final Element element) {
        super(element);
        this.element = element;
    }

    public void load() {
        String name = element.getText();
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective.getId().replaceAll(" ", "").equalsIgnoreCase(name.replaceAll(" ", ""))) {
                this.objective = objective;
            }
        }
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective.getId().replaceAll(" ", "").toLowerCase().startsWith(name.replaceAll(" ", "").toLowerCase())) {
                this.objective = objective;
            }
        }
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective.getName().replaceAll(" ", "").equalsIgnoreCase(name.replaceAll(" ", ""))) {
                this.objective = objective;
            }
        }
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective.getName().replaceAll(" ", "").toLowerCase().startsWith(name.replaceAll(" ", "").toLowerCase())) {
                this.objective = objective;
            }
        }
        this.team = element.getAttributeValue("team") != null ?
                Teams.getTeamById(element.getAttributeValue("team")).orNull() : null;
        this.any = Numbers.parseBoolean(element.getAttributeValue("any"), false);
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
