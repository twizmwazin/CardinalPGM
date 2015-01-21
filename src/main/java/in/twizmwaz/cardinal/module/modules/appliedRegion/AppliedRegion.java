package in.twizmwaz.cardinal.module.modules.appliedRegion;


import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class AppliedRegion implements Module {

    private final RegionModule region;
    private final FilterModule filter;

    public AppliedRegion(RegionModule region, FilterModule filter) {
        this.region = region;
        this.filter = filter;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onEvent(Event event) {

    }

}
