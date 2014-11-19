package in.twizmwaz.cardinal.regions.type;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.EmptyParser;

/**
 * Created by kevin on 10/26/14.
 */
public class EmptyRegion extends Region {

    public EmptyRegion(EmptyParser parser) {
    }

    @Override
    public boolean contains(BlockRegion region) {
        return false;
    }
}
