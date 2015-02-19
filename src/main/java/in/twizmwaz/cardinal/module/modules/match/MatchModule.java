package in.twizmwaz.cardinal.module.modules.match;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.ChatUtils;
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
        ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #"));
        ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # " + ChatColor.GOLD + "{0}" + ChatColor.DARK_PURPLE + " # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTED)));
        ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #"));
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        ChatUtils.getGlobalChannel().sendMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # #");
        ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# #    " + ChatColor.GOLD + "{0}" + ChatColor.DARK_PURPLE + "    # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_OVER)));
        if (event.getTeam() != null) {
            if (event.getTeam().size() == 1) {
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # {0}" + ChatColor.DARK_PURPLE + " # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().getColor() + ((Player) event.getTeam().get(0)).getDisplayName())));
            } else {
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # {0}" + ChatColor.DARK_PURPLE + " # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().getCompleteName())));
            }
        }
        ChatUtils.getGlobalChannel().sendMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # #");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, ChatColor.AQUA + event.getMatch().getLoadedMap().getName())));
        for (Player player : Bukkit.getOnlinePlayers()) {
            TeamUtils.getTeamById("observers").add(player, true, false);
        }
    }
}
