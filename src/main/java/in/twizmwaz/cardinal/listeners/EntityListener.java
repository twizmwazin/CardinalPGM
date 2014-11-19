package in.twizmwaz.cardinal.listeners;

import in.twizmwaz.cardinal.Cardinal;
import org.bukkit.event.Listener;

public class EntityListener implements Listener {

    private final Cardinal plugin;

    public EntityListener(Cardinal plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}
