package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class ObjectiveFilter extends Filter {

    @Override
    public FilterState getState(final Object o) {
        Bukkit.getLogger().log(Level.INFO, "Objective Filters are not yet supported in CardinalPGM.");
        return ABSTAIN;
    }

}
