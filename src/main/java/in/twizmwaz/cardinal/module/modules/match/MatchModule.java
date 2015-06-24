package in.twizmwaz.cardinal.module.modules.match;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.snowflakes.Snowflakes;
import in.twizmwaz.cardinal.module.modules.stats.MatchTracker;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
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
    public void onMatchEnd(MatchEndEvent event) {
        if (event.getTeam() != null) {
            if (event.getTeam().size() == 1) {
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().getColor() + ((Player) event.getTeam().get(0)).getName() + ChatColor.WHITE + ""));
                //sendTitle.((Player) event.getTeam().get(0)).getName() + ChatColor.WHITE + " wins!");
            } else {
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().getCompleteName() + ChatColor.WHITE + ""));
                //sendTitle.(event.getTeam().getCompleteName() + ChatColor.WHITE + " wins!");
            }
        }
        TeamModule winner = TimeLimit.getMatchWinner();
        /*if (event.getTeam() == winner) {
            sendMessage.(ChatColor.GREEN + "Your team won!");
            sendTitle. (ChatColor.GREEN + "Your team won!");
        } else {
            sendMessage.(ChatColor.RED + "Your team lost");
            sendTitle. (ChatColor.RED + "Your team lost");
        }*/
        if (Cardinal.getInstance().getConfig().getBoolean("auto-whitelist")) {
            Bukkit.getServer().setWhitelist(false);
            ChatUtils.getGlobalChannel().sendMessage(ChatColor.GREEN + "The whitelist is now " + ChatColor.RED + "disabled.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, ChatColor.AQUA + event.getMatch().getLoadedMap().getName())));
        for (Player player : Bukkit.getOnlinePlayers()) {
            TeamUtils.getTeamById("observers").add(player, true, false);
        }
    }
}
