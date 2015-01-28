package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ObjectiveFilterParser;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class ObjectiveFilter extends FilterModule {

    private final GameObjective objective;

    public ObjectiveFilter(final ObjectiveFilterParser parser) {
        super(parser.getName());
        this.objective = parser.getObjective();
    }

    @Override
    public FilterState evaluate(final Object object) {
        if (objective.isComplete()) return ALLOW;
        else return DENY;
    }

}
