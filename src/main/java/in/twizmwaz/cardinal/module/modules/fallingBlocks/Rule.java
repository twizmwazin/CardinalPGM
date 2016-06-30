package in.twizmwaz.cardinal.module.modules.fallingBlocks;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;

public class Rule {

    private FilterModule filter;
    private FilterModule sticky;
    private int delay;

    Rule(FilterModule filter, FilterModule sticky, int delay) {
        this.filter = filter;
        this.sticky = sticky;
        this.delay = delay;
    }

    public FilterModule getFilter() {
        return filter;
    }

    public FilterModule getSticky() {
        return sticky;
    }

    public int getDelay() {
        return delay;
    }

}
