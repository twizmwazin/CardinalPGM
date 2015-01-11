package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.parsers.MobFilterParser;
import org.bukkit.entity.EntityType;

public class MobFilter extends EntityFilter {

    public MobFilter(final EntityType mob) {
        super(mob);
    }
    
    public MobFilter(final MobFilterParser parser) {
        super(parser.getMobType());
    }

}
