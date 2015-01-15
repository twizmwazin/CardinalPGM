package in.twizmwaz.cardinal.module;

public @interface ModuleData {

    /**
     * @return The position of when the module should be loaded
     */
    public ModuleLoadTime load() default ModuleLoadTime.NORMAL;
    
}
