package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class CrouchingFilter extends FilterModule {

    public CrouchingFilter(FilterParser parser) {
        super(parser.getName(), parser.getParent());
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Player) {
                if (((Player) object).isSneaking())
                    return ALLOW;
                else
                    return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
