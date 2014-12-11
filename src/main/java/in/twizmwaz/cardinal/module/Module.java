package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module implements Listener {

    protected static final JavaPlugin plugin = GameHandler.getGameHandler().getPlugin();

    public abstract void unload();

}
