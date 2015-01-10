package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import in.twizmwaz.cardinal.module.GameObjective;
import org.bukkit.event.Event;

import static in.twizmwaz.cardinal.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.filter.FilterState.DENY;

public class ObjectiveFilter extends Filter {

    private final GameObjective objective;

    public ObjectiveFilter(final GameObjective objective) {
        this.objective = objective;
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (objective.isComplete()) return ALLOW;
        else return DENY;
    }

}
