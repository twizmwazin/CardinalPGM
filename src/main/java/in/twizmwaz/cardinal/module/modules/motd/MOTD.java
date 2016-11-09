package in.twizmwaz.cardinal.module.modules.motd;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.Config;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerListPingEvent;

public class MOTD implements Module {

    private final Match match;

    protected MOTD(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        String name = match.getLoadedMap().getName();
        ChatColor color = ChatColor.GRAY;
        switch (match.getState()) {
            case ENDED:
                color = ChatColor.AQUA;
                break;
            case PLAYING:
                color = ChatColor.GOLD;
                break;
            case STARTING:
                color = ChatColor.GREEN;
                break;
        }
        event.setMotd(color + "\u00BB " + ChatColor.AQUA + name + color + " \u00AB" +
                (!Config.motdMessage.equals("") ? "\n" + ChatColor.translateAlternateColorCodes('`', Config.motdMessage) : ""));

    }
}
