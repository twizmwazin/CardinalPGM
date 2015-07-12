package in.twizmwaz.cardinal.module.modules.match;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
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
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #"));
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # " + ChatColor.GOLD + "{0}" + ChatColor.DARK_PURPLE + " # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTED)));
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #"));
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        ChatUtil.getGlobalChannel().sendMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # #");
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# #    " + ChatColor.GOLD + "{0}" + ChatColor.DARK_PURPLE + "    # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_OVER)));
        if (event.getTeam().isPresent()) {
            if (event.getTeam().get().size() == 1) {
                ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # {0}" + ChatColor.DARK_PURPLE + " # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().get().getColor() + (event.getTeam().get()).getName())));
            } else {
                ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # {0}" + ChatColor.DARK_PURPLE + " # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().get().getCompleteName())));
            }
        }
        ChatUtil.getGlobalChannel().sendMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # #");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, ChatColor.AQUA + event.getMatch().getLoadedMap().getName())));
        for (Player player : Bukkit.getOnlinePlayers()) {
            Teams.getTeamById("observers").get().add(player, true, false);
        }
    }
}
