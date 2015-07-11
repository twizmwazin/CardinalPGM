package in.twizmwaz.cardinal.module.modules.motd;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerListPingEvent;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.util.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class MOTD implements Module {

    private final Match match;

    protected MOTD(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerListPing(ServerListPingEvent event) {
        try {
            String name = match.getLoadedMap().getName();
            if (match.getState() == MatchState.ENDED) {
                event.setMotd(ChatColor.RED + "\u00BB " + ChatColor.AQUA + name + ChatColor.RED + " \u00AB" + ChatColor.DARK_GRAY + " -" + ChatColor.WHITE + ChatColor.BOLD + " CardinalPGM" + "\n" + ChatColor.GOLD + "      Team: " + ChatColor.BLUE + Cardinal.getInstance().getConfig().getString("team-name"));
            } else if (match.getState() == MatchState.PLAYING) {
                event.setMotd(ChatColor.GOLD + "\u00BB " + ChatColor.AQUA + name + ChatColor.GOLD + " \u00AB" + ChatColor.DARK_GRAY + " -" + ChatColor.WHITE + ChatColor.BOLD + " CardinalPGM" + "\n" + ChatColor.GOLD + "      Team: " + ChatColor.BLUE + Cardinal.getInstance().getConfig().getString("team-name"));
            } else if (match.getState() == MatchState.STARTING) {
                event.setMotd(ChatColor.GREEN + "\u00BB " + ChatColor.AQUA + name + ChatColor.GREEN + " \u00AB" + ChatColor.DARK_GRAY + " -" + ChatColor.WHITE + ChatColor.BOLD + " CardinalPGM" + "\n" + ChatColor.GOLD + "      Team: " + ChatColor.BLUE + Cardinal.getInstance().getConfig().getString("team-name"));
            } else {
                event.setMotd(ChatColor.GRAY + "\u00BB " + ChatColor.AQUA + name + ChatColor.GRAY + " \u00AB" + ChatColor.DARK_GRAY + " -" + ChatColor.WHITE + ChatColor.BOLD + " CardinalPGM" + "\n" + ChatColor.GOLD + "      Team: " + ChatColor.BLUE + Cardinal.getInstance().getConfig().getString("team-name"));
            }
        } catch (NullPointerException ex) {

        }
    }
}
