package in.twizmwaz.cardinal.module;

import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ModuleCollection<M extends Module> extends ArrayList<M> {

    /**
     * Create a new {@link in.twizmwaz.cardinal.module}
     */
    public ModuleCollection() {
    }

    /**
     * @param collection The collection to create this with
     */
    public ModuleCollection(Collection<M> collection) {
        super(collection);
    }

    /**
     * @param modules The collection to create this with
     */
    @SafeVarargs
    public ModuleCollection(M... modules) {
        Collections.addAll(this, modules);
    }

    /**
     * Used for getting a single module. If there are multiple modules of the specified type, only one will be returned.
     *
     * @param clazz Class that represents the module to be returned
     * @param <T>   Module type to be filtered
     * @return A module of the specified type
     */
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        for (M module : this) {
            if (clazz.isInstance(module)) return ((T) module);
        }
        return null;
    }

    /**
     * Used to get all modules of a certain type
     *
     * @param clazz Class which represents the modules
     * @param <T>   Module type to be filtered
     * @return A new module
     */
    @SuppressWarnings("unchecked")
    public <T extends Module> ModuleCollection<T> getModules(Class<T> clazz) {
        ModuleCollection<T> results = new ModuleCollection<T>();
        for (Module module : this) {
            if (clazz.isInstance(module)) results.add((T) module);
        }
        return results;
    }

    /**
     * @return Random module from this
     */
    @SuppressWarnings("unchecked")
    public M getRandom() {
        ModuleCollection copy = (ModuleCollection) this.clone();
        Collections.shuffle(copy);
        return (M) copy.get(0);

    }

    /**
     * Unregister all modules from this
     */
    public void unregisterAll() {
        for (M m : this) {
            HandlerList.unregisterAll(m);
        }

    }
}
