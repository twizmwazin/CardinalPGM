package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class ClassFilter extends Filter {

    @Override
    public FilterState getState(Object o) {
        Bukkit.getLogger().log(Level.INFO, "Class Filters are not yet supported in CardinalPGM.");
        return ABSTAIN;
    }

}
