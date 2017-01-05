package in.twizmwaz.cardinal.module;

public enum ModuleLoadTime implements Comparable<ModuleLoadTime> {

    /**
     * First module to be loaded
     */
    EARLIEST(),
    /**
     * Module loaded very early
     */
    EARLIER(),
    /**
     * Module loaded before most modules
     */
    EARLY(),
    /**
     * Default time for a module to load
     */
    NORMAL(),
    /**
     * Module loaded after most modules
     */
    LATE(),
    /**
     * Module loaded very late
     */
    LATER(),
    /**
     * Last module to be loaded
     */
    LATEST();

}
