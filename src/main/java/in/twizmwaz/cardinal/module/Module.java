package in.twizmwaz.cardinal.module;

import org.bukkit.event.Listener;

public abstract class Module implements Listener {

    public ModuleInfo getInfo() {
        return this.getClass().getAnnotation(ModuleInfo.class);
    }

    public String getName() {
        return this.getClass().getAnnotation(ModuleInfo.class).name();
    }

    public Class getBuilder() {
        return this.getClass().getAnnotation(ModuleInfo.class).builder();
    }

}
