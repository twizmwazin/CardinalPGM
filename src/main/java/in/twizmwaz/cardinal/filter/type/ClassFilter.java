package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.util.logging.Level;

import static in.twizmwaz.cardinal.filter.FilterState.ABSTAIN;

public class ClassFilter extends Filter {

    @Override
    public FilterState evaluate(final Event event) {
        Bukkit.getLogger().log(Level.INFO, "Class Filters are not yet supported in CardinalPGM.");
        return ABSTAIN;
    }

}
