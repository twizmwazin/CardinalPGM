package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.block.Block;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class BlockFilter extends Filter {

    private final Block block;

    public BlockFilter(final Block block) {
        this.block = block;
    }

    @Override
    public FilterState getState(final Object o) {
        if (o instanceof Block) {
            if (o.equals(block)) return ALLOW;
            else return DENY;
        }
        else return ABSTAIN;
    }

}