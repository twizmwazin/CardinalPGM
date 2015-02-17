package in.twizmwaz.cardinal.module.modules.match;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.chatChannels.GlobalChannel;
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
        GlobalChannel channel = match.getModules().getModule(GlobalChannel.class);
        channel.sendMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #");
        channel.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # " + ChatColor.GOLD + "{0}" + ChatColor.DARK_PURPLE + " # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTED)));
        channel.sendMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # # # # # #");
        
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        GlobalChannel channel = match.getModules().getModule(GlobalChannel.class);
        channel.sendMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # #");
        channel.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# #    " + ChatColor.GOLD + "{0}" + ChatColor.DARK_PURPLE + "    # #", ChatConstant.UI_MATCH_OVER.asMessage()));
        if (event.getTeam() != null) {
            if (event.getTeam().size() == 1) {
                channel.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # {0}" + ChatColor.DARK_PURPLE + " # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, new UnlocalizedChatMessage(event.getTeam().getColor() + ((Player) event.getTeam().get(0)).getDisplayName()))));
            } else {
                channel.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "# # {0}" + ChatColor.DARK_PURPLE + " # #", new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, new UnlocalizedChatMessage(event.getTeam().getCompleteName()))));
            }
        }
        channel.sendMessage(ChatColor.DARK_PURPLE + "# # # # # # # # # # # #");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        GlobalChannel channel = match.getModules().getModule(GlobalChannel.class);
        channel.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, new UnlocalizedChatMessage(ChatColor.AQUA + event.getMatch().getLoadedMap().getName()))));
        for (Player player : Bukkit.getOnlinePlayers()) {
            TeamUtils.getTeamById("observers").add(player, true);
        }
    }
}
