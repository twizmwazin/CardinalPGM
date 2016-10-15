package in.twizmwaz.cardinal.module.modules.match;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import net.md_5.bungee.api.chat.TextComponent;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMatchEnd(MatchEndEvent event) {
        if (event.getTeam().isPresent()) {
            ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(event.getTeam().get().isPlural() ? ChatConstant.UI_MATCH_WIN : ChatConstant.UI_MATCH_WINS, event.getTeam().get().getCompleteName() + ChatColor.WHITE)));
            for (Player player : Bukkit.getOnlinePlayers()) {
                String title = new LocalizedChatMessage(event.getTeam().get().isPlural() ? ChatConstant.UI_MATCH_WIN : ChatConstant.UI_MATCH_WINS, event.getTeam().get().getCompleteName() + ChatColor.WHITE).getMessage(player.getLocale());
                if (Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get() == event.getTeam().get()) {
                    player.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_TEAM_WIN)).getMessage(player.getLocale()));
                    String subtitle = new LocalizedChatMessage(ChatConstant.UI_MATCH_TEAM_WIN).getMessage(player.getLocale());
                    player.showTitle(new TextComponent(title),new TextComponent(ChatColor.GREEN + subtitle), 0, 40, 30);
                } else if (!Teams.getTeamByPlayer(player).get().isObserver()) {
                    player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_TEAM_LOSE)).getMessage(player.getLocale()));
                    String subtitle = new LocalizedChatMessage(ChatConstant.UI_MATCH_TEAM_LOSE).getMessage(player.getLocale());
                    player.showTitle(new TextComponent(title), new TextComponent(ChatColor.RED + subtitle), 0, 40, 30);
                } else {
                    player.showTitle(new TextComponent(title), new TextComponent(""), 0, 40, 30);
                }
            }
        } else if (event.getPlayer().isPresent()){
            ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(event.getTeam().get().isPlural() ? ChatConstant.UI_MATCH_WIN : ChatConstant.UI_MATCH_WINS, ChatColor.YELLOW + (event.getPlayer().get()).getName() + ChatColor.WHITE)));
            for (Player player : Bukkit.getOnlinePlayers()) {
                String title = new LocalizedChatMessage(event.getTeam().get().isPlural() ? ChatConstant.UI_MATCH_WIN : ChatConstant.UI_MATCH_WINS, ChatColor.YELLOW + (event.getPlayer().get()).getName() + ChatColor.WHITE).getMessage(player.getLocale());
                player.showTitle(new TextComponent(title),new TextComponent(""), 0, 40, 30);
            }
        } else {
            ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_OVER)));
            for (Player player : Bukkit.getOnlinePlayers()) {
                String title = new LocalizedChatMessage(ChatConstant.UI_MATCH_OVER).getMessage(player.getLocale());
                player.showTitle(new TextComponent(title),new TextComponent(""), 0, 20, 20);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, ChatColor.AQUA + event.getMatch().getLoadedMap().getName())));
        for (Player player : Bukkit.getOnlinePlayers()) {
            Teams.getTeamById("observers").get().add(player, true, false);
        }
    }

}