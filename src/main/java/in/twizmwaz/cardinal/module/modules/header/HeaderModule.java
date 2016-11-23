package in.twizmwaz.cardinal.module.modules.header;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.event.RankChangeEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.repository.LoadedMap;
import in.twizmwaz.cardinal.util.ChatUtil;
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
import java.util.stream.Collectors;

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
        if (GameHandler.getGameHandler().getMatch().isRunning() && last != (int) MatchTimer.getTimeInSeconds()) {
            last = (int) MatchTimer.getTimeInSeconds();
            updateAll(HeaderPart.FOOTER);
        }
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        updateAll(HeaderPart.BOTH);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateAll(HeaderPart.HEADER);
    }

    @EventHandler
    public void onPlayerNameChange(PlayerNameUpdateEvent event) {
        updateAll(HeaderPart.HEADER);
    }

    @EventHandler
    public void onRankChange(RankChangeEvent event) {
        updateAll(HeaderPart.HEADER);
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        last = 0;
        updateAll(HeaderPart.FOOTER);
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        updateAll(HeaderPart.FOOTER);
    }

    @EventHandler
    public void onLangChange(PlayerLocaleChangeEvent event) {
        updatePlayer(event.getPlayer(), event.getNewLocale(), HeaderPart.NONE);
    }

    public void updateHeader() {
        header = new LocalizedChatMessage(ChatConstant.MISC_BY,
                new UnlocalizedChatMessage("" + ChatColor.AQUA + ChatColor.BOLD + mapName + ChatColor.DARK_GRAY),
                ChatUtil.toChatMessage(authors.stream()
                        .map(Contributor::getDisplayName).collect(Collectors.toList()), ChatColor.RESET, ChatColor.DARK_GRAY));
    }

    public void updateFooter() {
        footer = new UnlocalizedChatMessage(ChatColor.BOLD + message + ChatColor.RESET +
                ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "{0}: " +
                (GameHandler.getGameHandler().getMatch().isRunning() ? ChatColor.GREEN : ChatColor.GOLD) +
                Strings.formatTime(MatchTimer.getTimeInSeconds()) + ChatColor.DARK_GRAY + " - " +
                ChatColor.WHITE + ChatColor.BOLD + "Cardinal", ChatConstant.UI_TIME.asMessage());
    }

    public void updateAll(HeaderPart part) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(player, player.getLocale(), part);
        }
    }

    private void updatePlayer(Player player, String locale, HeaderPart part) {
        if (header == null || part.equals(HeaderPart.HEADER) || part.equals(HeaderPart.BOTH)) updateHeader();
        if (footer == null || part.equals(HeaderPart.FOOTER) || part.equals(HeaderPart.BOTH)) updateFooter();
        player.setPlayerListHeaderFooter(new TextComponent(header.getMessage(locale)), new TextComponent(footer.getMessage(locale)));
    }

    public enum HeaderPart {
        NONE,
        HEADER,
        FOOTER,
        BOTH;
    }

}
