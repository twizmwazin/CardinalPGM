package in.twizmwaz.cardinal.module.modules.match;

import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class MatchModule implements Module {

    private final Match match;

    protected MatchModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # " + ChatColor.GOLD + "The match has started!" + ChatColor.DARK_PURPLE + " # #");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #");
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # #");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# #    " + ChatColor.GOLD + "Game over!" + ChatColor.DARK_PURPLE + "    # #");
        try {
            if (event.getTeam().size() == 1) {
                String player = null;
                for (Object teammate : event.getTeam()) {
                    player = ((Player) teammate).getDisplayName();
                }
                Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # " + event.getTeam().getColor() + player + " wins!" + ChatColor.DARK_PURPLE + " # #");
            } else {
                Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # " + event.getTeam().getColor() + event.getTeam().getName() + " wins!" + ChatColor.DARK_PURPLE + " # #");
            }
        } catch (NullPointerException ex) {

        }
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # #");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Cycled to " + ChatColor.AQUA + event.getMatch().getMapInfo().getName());
        for (Player player : Bukkit.getOnlinePlayers()) {
            TeamUtils.getTeamById("observers").add(player, true);
        }
    }
}
