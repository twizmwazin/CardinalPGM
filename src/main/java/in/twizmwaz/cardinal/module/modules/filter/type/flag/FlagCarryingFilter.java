package in.twizmwaz.cardinal.module.modules.filter.type.flag;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.FlagFilterParser;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;

public class FlagCarryingFilter extends FlagFilter {

    public FlagCarryingFilter(FlagFilterParser parser) {
        super(parser);
    }

    @Override
    public FilterState evaluate(Object... objects) {
        for (Object object : objects) {
            if (object instanceof Player) {
                if (this.getFlag().isCarried() && this.getFlag().getPicker().equals(object)) return FilterState.ALLOW;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
