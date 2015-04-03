package in.twizmwaz.cardinal.module.modules.filter.type.old;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ChildrenFilterParser;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class AllowFilter extends FilterModule {

    private final ModuleCollection<FilterModule> children;

    public AllowFilter(final ChildrenFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.children = parser.getChildren();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        boolean abstain = true;
        if (children != null) {
            for (FilterModule child : children) {
                if (!child.evaluate(objects).equals(ABSTAIN)) abstain = false;
                if (child.evaluate(objects).equals(ALLOW)) return ALLOW;
            }
        }
        if (abstain) return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
        return DENY;
    }

}
