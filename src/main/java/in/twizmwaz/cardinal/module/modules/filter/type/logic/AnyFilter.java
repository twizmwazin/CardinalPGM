package in.twizmwaz.cardinal.module.modules.filter.type.logic;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ChildrenFilterParser;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class AnyFilter extends FilterModule {

    private final ModuleCollection<FilterModule> children;

    public AnyFilter(final String name, final ModuleCollection<FilterModule> children) {
        super(name);
        this.children = children;
    }
    
    public AnyFilter(final ChildrenFilterParser parser) {
        this(parser.getName(), parser.getChildren());
    }

    @Override
    public FilterState evaluate(final Object object) {
        boolean abstain = true;
        for (FilterModule child : children) {
            if (!child.evaluate(object).equals(ABSTAIN)) abstain = false;
            if (child.evaluate(object).equals(ALLOW)) return ALLOW;
        }
        if (abstain) return ABSTAIN;
        return DENY;
    }

}
