package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class CrouchingFilter extends Filter {

    @Override
    public FilterState getState(Object o) {
        if (o instanceof Player) {
            if (((Player) o).isSneaking()) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}
