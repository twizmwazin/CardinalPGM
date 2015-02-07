package in.twizmwaz.cardinal.module.modules.filter.type.logic;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ChildrenFilterParser;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class OneFilter extends FilterModule {

    private final ModuleCollection<FilterModule> children;

    public OneFilter(final String name, final ModuleCollection<FilterModule> children) {
        super(name);
        this.children = children;
    }
    
    public OneFilter(final ChildrenFilterParser parser) {
        this(parser.getName(), parser.getChildren());
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        boolean found = false;
        boolean abstain = true;
        for (FilterModule child : children) {
            if (!child.evaluate(objects).equals(ABSTAIN)) abstain = false;
            if (child.evaluate(objects).equals(ALLOW)) {
                if (!found) found = true;
                else return DENY;
            }
        }
        if (found) return ALLOW;
        if (abstain) return ABSTAIN;
        return DENY;
    }

}
