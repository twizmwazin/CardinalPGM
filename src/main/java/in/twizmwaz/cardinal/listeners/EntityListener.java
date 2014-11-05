package in.twizmwaz.cardinal.listeners;

import in.twizmwaz.cardinal.Cardinal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class EntityListener implements Listener {

    private final Cardinal plugin;

    public EntityListener(Cardinal plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (false /* check if item should be removed */) {
            event.setCancelled(true);
        }
    }
}
