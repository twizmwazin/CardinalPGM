package in.twizmwaz.cardinal.module.modules.filter.type.old;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ChildrenFilterParser;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class DenyFilter extends FilterModule {

    private final ModuleCollection<FilterModule> children;

    public DenyFilter(final ChildrenFilterParser parser) {
        super(parser.getName());
        this.children = parser.getChildren();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        boolean abstain = true;
        for (FilterModule child : children) {
            if (child.evaluate(objects).equals(ALLOW)) return DENY;
            if (!child.evaluate(objects).equals(ABSTAIN)) abstain = false;
        }
        if (abstain) return ABSTAIN;
        return ALLOW;
    }
}
