package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.EntityFilterParser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

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
                if (entity.equals(((Entity) object).getType()))
                    return ALLOW;
                else
                    return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
