package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.BlockFilterParser;
import org.bukkit.Material;
import org.bukkit.block.Block;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class BlockFilter extends FilterModule {

    private final Material material;

    public BlockFilter(final BlockFilterParser parser) {
        super(parser.getName());
        this.material = parser.getMaterial();
    }

    @Override
    public FilterState evaluate(final Object object) {
        if (object instanceof Block) {
            if (((Block) object).getType().equals(material)) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}