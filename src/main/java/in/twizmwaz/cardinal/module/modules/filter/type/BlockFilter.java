package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.BlockFilterParser;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class BlockFilter extends FilterModule {

    private final Material material;

    public BlockFilter(final BlockFilterParser parser) {
        super(parser.getName());
        this.material = parser.getMaterial();
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof BlockEvent) {
            if (((BlockEvent) event).getBlock().getType().equals(material)) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}