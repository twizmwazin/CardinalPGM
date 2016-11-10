package in.twizmwaz.cardinal.module.modules.timers;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import in.twizmwaz.cardinal.util.bossBar.BossBars;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StartTimer extends Countdown {

    private Match match;
    private boolean forced;
    private UUID neededPlayers;

    public StartTimer(Match match) {
        super(BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.GREEN, BarStyle.SOLID, false), false);
        this.match = match;
        this.neededPlayers = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.RED, BarStyle.SOLID, false);
    }

    @Override
    public void unload() {
        BossBars.removeBroadcastedBossBar(neededPlayers);
        super.unload();
    }

    public Match getMatch() {
        return match;
    }

    public boolean startTimer(int time, boolean forced) {
        this.forced = forced;
        return startCountdown(time);
    }

    @Override
    public void onRun() {
        if (getTime() <= 3) {
            Players.broadcastSoundEffect(Sound.BLOCK_NOTE_PLING, 1, 1);
            Bukkit.getOnlinePlayers().stream().filter(player -> !Teams.getTeamByPlayer(player).get().isObserver())
                    .forEach(player -> player.showTitle(new TextComponent(ChatColor.YELLOW + "" + getTime()), new TextComponent(""), 0, 5, 15));
        }
    }

    @Override
    public boolean canStart() {
        return !match.isRunning() && !match.hasEnded();
    }

    @Override
    public void onCountdownStart() {
        if (getTime() >= 1) ChatUtil.getGlobalChannel().sendLocalizedMessage(getBossbarMessage());
        match.setState(MatchState.STARTING);
    }

    @Override
    public void onCountdownCancel() {
        match.setState(MatchState.WAITING);
    }

    @Override
    public void onCountdownEnd() {
        if (forced || enoughPlayers()) {
            Players.broadcastSoundEffect(Sound.BLOCK_NOTE_PLING, 1, 2);
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (!Teams.getTeamByPlayer(player).get().isObserver()) {
                    String title = new LocalizedChatMessage(ChatConstant.UI_MATCH_START_TITLE).getMessage(player.getLocale());
                    player.showTitle(new TextComponent(net.md_5.bungee.api.ChatColor.GREEN + title), new TextComponent(""), 0, 5, 15);
                }
            });
            match.setState(MatchState.PLAYING);
            ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTED)));
            Bukkit.getServer().getPluginManager().callEvent(new MatchStartEvent());
        } else {
            ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_NOT_ENOUGH_PLAYERS)));
            onCountdownCancel();
        }
    }

    private boolean enoughPlayers() {
        return Teams.getTeams().stream().noneMatch((team) -> !team.isObserver() && team.size() < team.getMin());
    }

    private int neededPlayers() {
        return Teams.getTeams().stream().mapToInt(team -> Math.max(0, team.getMin() - team.size())).sum();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        updateNeededPlayers();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeTeam(MatchStartEvent event) {
        BossBars.removeBroadcastedBossBar(neededPlayers);
    }

    public void updateNeededPlayers() {
        int neededPlayerCount = neededPlayers();
        boolean visible = (match.isWaiting() || match.isState(MatchState.STARTING)) && neededPlayerCount > 0;
        if (visible) BossBars.setTitle(neededPlayers, waitingPlayerMessage(neededPlayerCount));
        BossBars.setVisible(neededPlayers, visible);
    }

    @Override
    public ChatMessage getBossbarMessage() {
        return new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTING_IN, new LocalizedChatMessage(getTime() == 1 ? ChatConstant.UI_SECOND : ChatConstant.UI_SECONDS, ChatColor.DARK_RED + "" + getTime() + ChatColor.GREEN)));
    }

    private ChatMessage waitingPlayerMessage(int players) {
        List<TeamModule> teams = Teams.getTeams().stream()
                .filter((team) -> !team.isObserver() && team.size() < team.getMin()).limit(2).collect(Collectors.toList());
        return new UnlocalizedChatMessage(ChatColor.RED + "{0}",
                new LocalizedChatMessage(players == 1 ? ChatConstant.UI_WAITING_PLAYER : ChatConstant.UI_WAITING_PLAYERS,
                        ChatColor.AQUA + "" + players + ChatColor.RED,
                        teams.size() == 1 ? teams.get(0).getCompleteName() : ""));
    }

}