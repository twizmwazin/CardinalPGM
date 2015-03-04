package in.twizmwaz.cardinal.module.modules.header;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.util.Contributor;
import in.twizmwaz.cardinal.util.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class HeaderModule implements TaskedModule {
    
    private final ChatMessage header;
    private final String message;
    private int last;

    public HeaderModule(LoadedMap map) {
        this.last = 0;
        this.message = Cardinal.getInstance().getConfig().getString("server-message");
        this.header = new UnlocalizedChatMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + map.getName() + ChatColor.RESET.toString() + ChatColor.DARK_GRAY + " {0} {1}", ChatConstant.MISC_BY.asMessage(), assembleAuthors(map.getAuthors()));
    }


    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (Math.round(MatchTimer.getTimeInSeconds()) > last) {
            last = (int) Math.round(MatchTimer.getTimeInSeconds());
            for (Player player : Bukkit.getOnlinePlayers()) {
                updatePlayer(player);
            }
        }
    }
    
    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(player);
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayer(event.getPlayer());
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(player);
        }
    }
    
    private void updatePlayer(Player player) {
        StringBuilder footer = new StringBuilder()
                .append(ChatColor.BOLD)
                .append(message)
                .append(ChatColor.RESET)
                .append(ChatColor.DARK_GRAY)
                .append(" - ")
                .append(ChatColor.GRAY)
                .append(ChatConstant.UI_TIME.getMessage(player.getLocale()))
                .append(": ")
                .append(GameHandler.getGameHandler().getMatch().isRunning() ? ChatColor.GREEN : ChatColor.GOLD)
                .append(StringUtils.formatTime(MatchTimer.getTimeInSeconds()))
                .append(ChatColor.DARK_GRAY)
                .append(" - ")
                .append(ChatColor.WHITE)
                .append(ChatColor.BOLD)
                .append("Cardinal");
        player.setPlayerListHeaderFooter(new TextComponent(header.getMessage(player.getLocale())), new TextComponent(footer.toString()));
    }
    
    private ChatMessage assembleAuthors(List<Contributor> authors) {
        StringBuilder builder = new StringBuilder();
        if (authors.size() == 1) builder.append(ChatColor.GRAY).append(authors.get(0).getName());
        else if (authors.size() > 1) {
            for (Contributor author : authors) {
                if (authors.indexOf(author) < authors.size() - 2) {
                    builder.append(ChatColor.GRAY).append(author.getName()).append(ChatColor.DARK_GRAY).append(", ");
                } else if (authors.indexOf(author) == authors.size() - 2) {
                    builder.append(ChatColor.GRAY).append(author.getName()).append(ChatColor.DARK_GRAY).append(" {0} ");
                } else if (authors.indexOf(author) == authors.size() - 1) {
                    builder.append(ChatColor.GRAY).append(author.getName());
                }
            }
        }
        return new UnlocalizedChatMessage(builder.toString(), ChatConstant.MISC_AND.asMessage());
    }
}
