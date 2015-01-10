package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockEvent;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class BlockFilter extends Filter {

    private final Material material;

    public BlockFilter(Material material) {
        this.material = material;
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof BlockEvent) {
            if (((BlockEvent) event).getBlock().getType().equals(material)) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}