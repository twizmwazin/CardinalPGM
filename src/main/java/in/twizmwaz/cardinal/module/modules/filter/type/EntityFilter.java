package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.EntityFilterParser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class EntityFilter extends FilterModule {

    private final EntityType entity;

    public EntityFilter(final EntityFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.entity = parser.getEntityType();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Entity) {
                if (((Entity) object).getType().equals(entity))
                    return getParent() == null ? ALLOW : (getParent().evaluate(objects).equals(DENY) ? DENY : ALLOW);
                else
                    return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
