package in.twizmwaz.cardinal.module.modules.filter.type.old;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.event.Event;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class AllowFilter extends FilterModule {

    private final ModuleCollection<FilterModule> children;

    public AllowFilter(final String name, final ModuleCollection<FilterModule> children) {
        super(name);
        this.children = children;
    }

    @Override
    public FilterState evaluate(final Event event) {
        for (FilterModule child : children) {
            if (child.evaluate(event).equals(ALLOW)) return ALLOW;
        }
        return DENY;
    }

}
