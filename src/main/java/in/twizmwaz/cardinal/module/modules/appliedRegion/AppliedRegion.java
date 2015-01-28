package in.twizmwaz.cardinal.module.modules.appliedRegion;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.bukkit.event.HandlerList;

public abstract class AppliedRegion implements Module {

    private final RegionModule region;

    public AppliedRegion(RegionModule region) {
        this.region = region;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public RegionModule getRegion() {
        return region;
    }
}
