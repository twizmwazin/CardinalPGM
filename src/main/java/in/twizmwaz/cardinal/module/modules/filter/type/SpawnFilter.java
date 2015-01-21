package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.SpawnFilterParser;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class SpawnFilter extends FilterModule {

    private final CreatureSpawnEvent.SpawnReason reason;

    public SpawnFilter(final SpawnFilterParser parser) {
        super(parser.getName());
        this.reason = parser.getReason();
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof CreatureSpawnEvent) {
            if (((CreatureSpawnEvent) event).equals(reason)) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}
