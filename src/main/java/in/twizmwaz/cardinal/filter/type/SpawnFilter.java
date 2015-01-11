package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import in.twizmwaz.cardinal.filter.parsers.SpawnFilterParser;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class SpawnFilter extends Filter {

    private final CreatureSpawnEvent.SpawnReason reason;

    public SpawnFilter(final CreatureSpawnEvent.SpawnReason reason) {
        this.reason = reason;
    }
    
    public SpawnFilter(final SpawnFilterParser parser) {
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
