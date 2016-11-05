package in.twizmwaz.cardinal.module.modules.header;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.event.RankChangeEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.util.Config;
import in.twizmwaz.cardinal.util.Contributor;
import in.twizmwaz.cardinal.util.Strings;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

import java.util.List;

public class HeaderModule implements TaskedModule {

    private ChatMessage header;
    private ChatMessage footer;
    private final String mapName;
    private final List<Contributor> authors;
    private final String message = ChatColor.translateAlternateColorCodes('`', Config.serverMessage);
    private int last = 0;

    public HeaderModule(LoadedMap map) {
        this.mapName = map.getName();
        this.authors = map.getAuthors();
    }


    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning() && last != (int)MatchTimer.getTimeInSeconds()) {
            last = (int)MatchTimer.getTimeInSeconds();
            updateFooter();
            updateAll();
        }
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        updateAll();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateHeader();
        updateAll();
    }

    @EventHandler
    public void onPlayerNameChange(PlayerNameUpdateEvent event) {
        updateHeader();
        updateAll();
    }

    @EventHandler
    public void onRankChange(RankChangeEvent event) {
        if (event.isOnline()) return;
        updateHeader();
        updateAll();
    }

    @EventHandler
    public void onMatchStart (MatchStartEvent event) {
        last = 0;
        updateFooter();
        updateAll();
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        updateFooter();
        updateAll();
    }

    @EventHandler
    public void onLangChange(PlayerLocaleChangeEvent event) {
        updatePlayer(event.getPlayer(), event.getNewLocale());
    }

    public void updateHeader() {
        header = new UnlocalizedChatMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + mapName + ChatColor.RESET.toString() + ChatColor.DARK_GRAY + " {0} {1}", ChatConstant.MISC_BY.asMessage(), assembleAuthors());
    }

    public void updateFooter() {
        footer = new UnlocalizedChatMessage(ChatColor.BOLD + message + ChatColor.RESET +
                ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "{0}: " +
                (GameHandler.getGameHandler().getMatch().isRunning() ? ChatColor.GREEN : ChatColor.GOLD) +
                Strings.formatTime(MatchTimer.getTimeInSeconds()) + ChatColor.DARK_GRAY + " - " +
                ChatColor.WHITE + ChatColor.BOLD + "Cardinal", ChatConstant.UI_TIME.asMessage());
    }

    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(player, player.getLocale());
        }
    }

    private void updatePlayer(Player player, String locale) {
        if (header == null) updateHeader();
        if (footer == null) updateFooter();
        player.setPlayerListHeaderFooter(new TextComponent(header.getMessage(locale)), new TextComponent(footer.getMessage(locale)));
    }

    private ChatMessage assembleAuthors() {
        StringBuilder builder = new StringBuilder();
        if (authors.size() == 1) builder.append(authors.get(0).getDisplayName());
        else if (authors.size() > 1) {
            for (Contributor author : authors) {
                if (authors.indexOf(author) < authors.size() - 2) {
                    builder.append(author.getDisplayName()).append(ChatColor.DARK_GRAY).append(", ");
                } else if (authors.indexOf(author) == authors.size() - 2) {
                    builder.append(author.getDisplayName()).append(ChatColor.DARK_GRAY).append(" {0} ");
                } else if (authors.indexOf(author) == authors.size() - 1) {
                    builder.append(author.getDisplayName());
                }
            }
        }
        return new UnlocalizedChatMessage(builder.toString(), ChatConstant.MISC_AND.asMessage());
    }

}
