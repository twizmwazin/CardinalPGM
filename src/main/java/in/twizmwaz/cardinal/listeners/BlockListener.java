package in.twizmwaz.cardinal.listeners;

import in.twizmwaz.cardinal.Cardinal;
import org.bukkit.event.Listener;

public class BlockListener implements Listener {

    private final Cardinal plugin;

    public BlockListener(Cardinal plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
