package in.twizmwaz.cardinal.listeners;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.player.PgmPlayer;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PgmPlayer player = new PgmPlayer(event.getPlayer());
        event.setJoinMessage(player.getCompleteName() + ChatColor.YELLOW + " joined the game");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PgmPlayer player = PgmPlayer.getPgmPlayer(event.getPlayer());
        event.setQuitMessage(player.getCompleteName() + ChatColor.YELLOW + " left the game");
        PgmPlayer.getPgmPlayer(event.getPlayer()).remove();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        PgmPlayer player = PgmPlayer.getPgmPlayer(event.getPlayer());
        player.remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerListPing(ServerListPingEvent event) {
        Match match = GameHandler.getGameHandler().getMatch();
        try {
            String name = match.getMapInfo().getName();
            if (match.getState() == MatchState.ENDED) {
                event.setMotd(ChatColor.RED + "\u00BB " + ChatColor.AQUA + name + ChatColor.RED + " \u00AB");
            } else if (match.getState() == MatchState.PLAYING) {
                event.setMotd(ChatColor.GOLD + "\u00BB " + ChatColor.AQUA + name + ChatColor.GOLD + " \u00AB");
            } else if (match.getState() == MatchState.STARTING) {
                event.setMotd(ChatColor.GREEN + "\u00BB " + ChatColor.AQUA + name + ChatColor.GREEN + " \u00AB");
            } else {
                event.setMotd(ChatColor.GRAY + "\u00BB " + ChatColor.AQUA + name + ChatColor.GRAY + " \u00AB");
            }
        } catch (NullPointerException ex) {

        }
    }
}
