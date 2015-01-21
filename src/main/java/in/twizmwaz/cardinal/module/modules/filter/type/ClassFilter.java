package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.util.logging.Level;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;

public class ClassFilter extends FilterModule {

    public ClassFilter(FilterParser parser) {
        super(parser.getName());
    }

    @Override
    public FilterState evaluate(final Event event) {
        Bukkit.getLogger().log(Level.INFO, "Class Filters are not yet supported in CardinalPGM.");
        return ABSTAIN;
    }

}
