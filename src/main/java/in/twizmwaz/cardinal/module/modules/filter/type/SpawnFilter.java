package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.SpawnFilterParser;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class SpawnFilter extends FilterModule {

    private final CreatureSpawnEvent.SpawnReason reason;

    public SpawnFilter(final SpawnFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.reason = parser.getReason();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof CreatureSpawnEvent) {
                if (((CreatureSpawnEvent) object).getSpawnReason().equals(reason))
                    return ALLOW;
                else
                    return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
