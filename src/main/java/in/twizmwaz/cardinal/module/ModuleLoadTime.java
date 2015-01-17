package in.twizmwaz.cardinal.module;

import java.util.ArrayList;
import java.util.List;

public enum ModuleLoadTime {

    /**
     * First module to be loaded
     */
    EARLIEST(0),
    /**
     * Module loaded very early
     */
    EARLIER(1),
    /**
     * Module loaded before most modules
     */
    EARLY(2),
    /**
     * Default time for a module to load
     */
    NORMAL(3),
    /**
     * Module loaded after most modules
     */
    LATE(4),
    /**
     * Module loaded very late
     */
    LATER(5),
    /**
     * Last module to be loaded
     */
    LATEST(6);

    private final int slot;

    private ModuleLoadTime(int slot) {
        this.slot = slot;
    }

    public static List<ModuleLoadTime> getOrdered() {
        List<ModuleLoadTime> results = new ArrayList<>(7);
        results.add(0, EARLIEST);
        results.add(1, EARLIER);
        results.add(2, EARLY);
        results.add(3, NORMAL);
        results.add(4, LATE);
        results.add(5, LATER);
        results.add(6, LATEST);
        return results;
    }

}
