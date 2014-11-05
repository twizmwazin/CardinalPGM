package in.twizmwaz.cardinal.listeners;

import in.twizmwaz.cardinal.Cardinal;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * Created by kevin on 10/25/14.
 */
public class ConnectionListener implements Listener {

    private final Cardinal plugin;

    public ConnectionListener(Cardinal plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //temp
        event.getPlayer().teleport(new Location(plugin.getGameHandler().getMatchWorld(), 0, 64, 0));
        //TwizPlayer player = new TwizPlayer(event.getPlayer());
        //event.setJoinMessage(player.getCompleteName() + YELLOW + " joined the game");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        //TwizPlayer player = TwizPlayer.getPlayer(event.getPlayer());
        //event.setQuitMessage(player.getCompleteName() + YELLOW + " left the game");
        //player.remove();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        //TwizPlayer.getPlayer(event.getPlayer()).remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerListPing(ServerListPingEvent event) {
        /*if (match.getMain() == null) {
            event.setMotd(ChatColor.GRAY + "\u00BB  \u00AB");
        } else {
            String name = match.getMain().getMap().getName();
            if (match.getMain().getState() == MatchState.ENDED) {
                event.setMotd(ChatColor.RED + "\u00BB " + ChatColor.AQUA + name + ChatColor.RED + " \u00AB");
            } else if (match.getMain().getState() == MatchState.PLAYING) {
                event.setMotd(ChatColor.GOLD + "\u00BB " + ChatColor.AQUA + name + ChatColor.GOLD + " \u00AB");
            } else if (match.getMain().getState() == MatchState.STARTING) {
                event.setMotd(ChatColor.GREEN + "\u00BB " + ChatColor.AQUA + name + ChatColor.GREEN + " \u00AB");
            } else {
                event.setMotd(ChatColor.GRAY + "\u00BB " + ChatColor.AQUA + name + ChatColor.GRAY + " \u00AB");
            }
        }*/
    }
}
