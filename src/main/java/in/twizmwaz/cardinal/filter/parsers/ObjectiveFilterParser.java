package in.twizmwaz.cardinal.filter.parsers;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.jdom2.Element;

public class ObjectiveFilterParser {

    private GameObjective objective;
    private PgmTeam team;
    private boolean any;

    public ObjectiveFilterParser(final Element element) {
        String name = element.getText();
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective.getName().equalsIgnoreCase(name)) this.objective = objective;
        }
        try {
            String team = element.getAttributeValue("team");
            this.team = GameHandler.getGameHandler().getMatch().getTeamById(team);
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

    public PgmTeam getTeam() {
        return team;
    }

    public boolean isAny() {
        return any;
    }
}
