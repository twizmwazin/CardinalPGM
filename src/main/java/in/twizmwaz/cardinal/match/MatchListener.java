package in.twizmwaz.cardinal.match;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by kevin on 11/19/14.
 */
public class MatchListener implements Listener {

    private final JavaPlugin plugin;

    public MatchListener(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # " + ChatColor.GOLD + "The match has started!" + ChatColor.DARK_PURPLE + " # #");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #");
    }



}
