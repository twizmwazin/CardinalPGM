package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.MobFilterParser;
import net.minecraft.server.EntityInsentient;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class MobFilter extends FilterModule {

    private final EntityType mobType;

    public MobFilter(final MobFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.mobType = parser.getMobType();
    }

    @Override
    public FilterState evaluate(Object... objects) {
        for (Object object : objects) {
            if (object instanceof Entity) {
                if (((CraftEntity)object).getHandle() instanceof EntityInsentient && mobType.equals(((CraftEntity) object).getType()))
                    return ALLOW;
                else
                    return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }
}
