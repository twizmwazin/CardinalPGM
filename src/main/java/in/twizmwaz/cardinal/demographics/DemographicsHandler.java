package in.twizmwaz.cardinal.demographics;

import in.twizmwaz.cardinal.Database;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;

public class DemographicsHandler implements Listener {
    
    private final JavaPlugin plugin;
    private Database database;
    
    public DemographicsHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        loadDatabase();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    private void loadDatabase() {
        File demographicsFile = new File(plugin.getDataFolder(), "demographics.xml");
        if (demographicsFile.exists()) {
            try {
                database = Database.loadFromFile(demographicsFile);
            } catch (JDOMException | IOException e) {
                e.printStackTrace();
            }
        } else {
            database = Database.newInstance(demographicsFile);
        }
    }
    
    public void saveAndReport() {
        database.save(new File(plugin.getDataFolder(), "demographics.xml"));
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new LookupService(event.getPlayer(), database), 0);
    }
    
    @EventHandler
    public void onLocaleChange(PlayerLocaleChangeEvent event) {
        database.put(event.getPlayer(), "lang", event.getPlayer().getLocale());
        
    }
}
