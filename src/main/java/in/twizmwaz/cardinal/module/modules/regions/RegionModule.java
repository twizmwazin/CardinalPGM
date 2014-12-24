package in.twizmwaz.cardinal.module.modules.regions;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.regions.Region;

import java.util.Map;

public class RegionModule implements Module {

    private Map<String, Region> regions;

    protected RegionModule(Map<String, Region> regions) {
        this.regions = regions;
    }

    @Override
    public void unload() {

    }

    public Region getNamedRegion(String name) {
        return regions.get(name);
    }

}
