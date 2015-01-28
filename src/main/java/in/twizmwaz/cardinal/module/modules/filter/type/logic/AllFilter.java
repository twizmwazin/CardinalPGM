package in.twizmwaz.cardinal.module.modules.filter.type.logic;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class AllFilter extends FilterModule {

    private final ModuleCollection<FilterModule> children;

    public AllFilter(final String name, final ModuleCollection<FilterModule> children) {
        super(name);
        this.children = children;
    }

    @Override
    public FilterState evaluate(final Object object) {
        for (FilterModule child : children) {
            if (child.evaluate(object).equals(DENY)) return DENY;
        }
        return ALLOW;
    }

}
