package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.GenericFilterParser;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class VoidFilter extends FilterModule {
    
    public VoidFilter(final GenericFilterParser parser) {
        super(parser.getName());
    }

    @Override
    public FilterState evaluate(final Object object) {
        if (object instanceof Block) {
            Block check = new Location(GameHandler.getGameHandler().getMatchWorld(), 
                    ((Block) object).getX(), 0, ((Block) object).getZ()).getBlock();
            return check.getType() == Material.AIR ? DENY : ALLOW;
        } else return ABSTAIN;
    }
}
