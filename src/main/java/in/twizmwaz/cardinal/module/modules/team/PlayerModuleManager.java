package in.twizmwaz.cardinal.module.modules.team;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.event.TeamNameChangeEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.scoreboard.ScoreboardModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class PlayerModuleManager extends TeamModule {

    private final Match match;
    private boolean colors;
    private Random random = new Random();

    private Map<ChatColor, Integer> usedColors;
    private static ChatColor[] allowedColors = new ChatColor[] {
            ChatColor.DARK_GREEN,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_RED,
            ChatColor.DARK_PURPLE,
            ChatColor.GOLD,
            ChatColor.BLUE,
            ChatColor.GREEN,
            ChatColor.RED,
            ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW
    };

    PlayerModuleManager(Match match, int min, int max, int maxOverfill, boolean colors) {
        super(match, "Players", "participants", min, max, maxOverfill, ChatColor.YELLOW, true, false);
        this.match = match;
        this.colors = colors;
        if (colors) {
            usedColors = new TreeMap<>();
            for (ChatColor color : allowedColors) {
                usedColors.put(color, 0);
            }
        }
    }

    @Override
    public String getName(String locale) {
        return ChatConstant.MISC_PLAYERS.getMessage(locale);
    }

    @Override
    public String getCompleteName(String locale) {
        return getColor() + ChatConstant.MISC_PLAYERS.getMessage(locale);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public boolean add(Player player, boolean force, boolean message) {
        if (Blitz.matchIsBlitz() && match.isRunning() && !force) {
            player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_MAY_NOT_JOIN, ChatColor.ITALIC + "" + ChatColor.AQUA + "Blitz" + ChatColor.RESET + ChatColor.RED)).getMessage(player.getLocale()));
            return false;
        }
        if (!force && size() >= getMax()) {
            player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_TEAM_FULL, "Players" + ChatColor.RED)).getMessage(player.getLocale()));
            return false;
        }
        Optional<TeamModule> oldTeam = Teams.getTeamOrPlayerManagerByPlayer(player);
        ChatColor color = colors ? getNewColor() : ChatColor.YELLOW;
        if (colors) usedColors.put(color, usedColors.get(color) + 1);
        PlayerModule playerModule = new PlayerModule(match, player, color);
        match.getModules().add(playerModule);
        match.getModules().add(new ScoreModule(playerModule));
        ScoreboardModule.addPlayerModule(playerModule);
        playerModule.add(player, false, false);
        PlayerChangeTeamEvent event = new PlayerChangeTeamEvent(player, force, Optional.of(this), oldTeam);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (message) {
            event.getPlayer().sendMessage(ChatColor.WHITE + new LocalizedChatMessage(ChatConstant.GENERIC_JOINED, ChatConstant.MISC_MATCH.asMessage()).getMessage(event.getPlayer().getLocale()));
        }
        return !event.isCancelled() || force;
    }

    @Override
    public void leave(Player player) {
        super.leave(player);
        PlayerModule playerModule = (PlayerModule) Teams.getTeamOrPlayerByPlayer(player).orNull();
        if (playerModule != null) {
            if (colors) usedColors.put(playerModule.getColor(), usedColors.get(playerModule.getColor()) - 1);
            match.getModules().remove(playerModule);
            match.getModules().remove(ScoreModule.getScoreModule(playerModule));
            ScoreboardModule.removePlayerModule(playerModule);
        }
    }

    private ChatColor getNewColor() {
        int min = Collections.min(usedColors.values());
        List<ChatColor> colors = Lists.newArrayList();
        for (ChatColor color : usedColors.keySet())
            if (usedColors.get(color) == min) colors.add(color);
        return colors.get(random.nextInt(colors.size()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNameChange(PlayerNameUpdateEvent event) {
        TeamModule team = Teams.getTeamOrPlayerByPlayer(event.getPlayer()).get();
        if (team instanceof PlayerModule) {
            team.setName(event.getPlayer().getPlayerListName());
            Bukkit.getServer().getPluginManager().callEvent(new TeamNameChangeEvent(team));
        }
    }

}
