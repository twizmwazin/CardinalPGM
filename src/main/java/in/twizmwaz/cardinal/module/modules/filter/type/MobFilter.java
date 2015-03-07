package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.MobFilterParser;
import org.bukkit.entity.CreatureType;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class MobFilter extends FilterModule {

    private final CreatureType mobType;
    
    public MobFilter(final MobFilterParser parser) {
        super(parser.getName());
        this.mobType = parser.getMobType();
    }

    @Override
    public FilterState evaluate(Object... objects) {
        for (Object object : objects) {
            if (object instanceof CreatureType) {
                if (((CreatureType) object).equals(mobType))
                    return ALLOW;
                else
                    return DENY;
            }
        }
        return ABSTAIN;
    }
}
