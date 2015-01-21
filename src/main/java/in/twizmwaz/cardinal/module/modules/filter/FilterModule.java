package in.twizmwaz.cardinal.module.modules.filter;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.Event;

public abstract class FilterModule implements Module {
    
    private final String name;

    /**
     * @param name The given name of the filter
     */
    protected FilterModule(String name) {
        this.name = name;
    }

    /**
     * @param event The event which will be filters
     * @return The state of the filter.
     */
    public abstract FilterState evaluate(final Event event);

    @Override
    public void unload() {

    }

    /**
     * @return The specified name of the filter, or null if there is none
     */
    public String getName() {
        return name;
    }
}
