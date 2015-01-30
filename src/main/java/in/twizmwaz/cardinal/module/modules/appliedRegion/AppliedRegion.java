package in.twizmwaz.cardinal.module.modules.appliedRegion;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.bukkit.event.HandlerList;

public abstract class AppliedRegion implements Module {

    protected final RegionModule region;
    protected final FilterModule filter;
    protected final String message;

    public AppliedRegion(RegionModule region, FilterModule filter, String message) {
        this.region = region;
        this.filter = filter;
        this.message = message;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
