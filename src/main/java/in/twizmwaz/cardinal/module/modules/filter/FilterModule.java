package in.twizmwaz.cardinal.module.modules.filter;

import in.twizmwaz.cardinal.module.Module;

public abstract class FilterModule implements Module {
    
    private final String name;
    private FilterModule parent;

    /**
     * @param name The given name of the filter
     */
    protected FilterModule(String name, FilterModule parent) {
        this.name = name;
        this.parent = parent;
    }

    /**
     * @param objects The objects which will be filtered
     * @return The state of the filter.
     */
    public abstract FilterState evaluate(final Object... objects);

    @Override
    public void unload() {

    }

    /**
     * @return The specified name of the filter, or null if there is none
     */
    public String getName() {
        return name;
    }

    public FilterModule getParent() {
        return parent;
    }
}
