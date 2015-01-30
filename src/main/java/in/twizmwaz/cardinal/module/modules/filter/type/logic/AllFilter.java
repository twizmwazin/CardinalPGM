package in.twizmwaz.cardinal.module.modules.filter.type.logic;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ChildrenFilterParser;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class AllFilter extends FilterModule {

    private final ModuleCollection<FilterModule> children;

    public AllFilter(final String name, final ModuleCollection<FilterModule> children) {
        super(name);
        this.children = children;
    }
    
    public AllFilter(final ChildrenFilterParser parser) {
        this(parser.getName(), parser.getChildren());
    }

    @Override
    public FilterState evaluate(final Object object) {
        boolean abstain = true;
        for (FilterModule child : children) {
            if (!child.evaluate(object).equals(ABSTAIN)) abstain = false;
            if (child.evaluate(object).equals(DENY)) return DENY;
        }
        if (abstain) return ABSTAIN;
        return ALLOW;
    }

}
