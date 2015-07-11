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
    public void onMatchEnd(MatchEndEvent event) {
        if (event.getTeam() != null) {
            if (event.getTeam().size() == 1) {
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage("{0}" , new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().getColor() + ((Player) event.getTeam().get(0)).getName() + ChatColor.WHITE)));
            } else {
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage("{0}" , new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().getCompleteName() + ChatColor.WHITE )));
            }
        } else {
			ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_OVER)));
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