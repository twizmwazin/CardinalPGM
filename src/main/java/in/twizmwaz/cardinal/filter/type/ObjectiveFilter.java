package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import in.twizmwaz.cardinal.module.GameObjective;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class ObjectiveFilter extends Filter {

    private final GameObjective objective;

    public ObjectiveFilter(final GameObjective objective) {
        this.objective = objective;
    }

    @Override
    public FilterState getState(final Object o) {
        if (objective.isComplete()) return ALLOW;
        else return DENY;
    }

}
