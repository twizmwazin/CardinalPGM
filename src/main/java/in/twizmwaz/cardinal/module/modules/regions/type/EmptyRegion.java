package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.module.modules.regions.Region;
import in.twizmwaz.cardinal.module.modules.regions.parsers.EmptyParser;

/**
 * Created by kevin on 10/26/14.
 */
public class EmptyRegion extends Region {

    public EmptyRegion(String name) {
        super(name);
    }

    public EmptyRegion(EmptyParser parser) {
        super(parser.getName());
    }

    public String getName() {
        return super.getName();
    }

    @Override
    public boolean contains(BlockRegion region) {
        return false;
    }
}
