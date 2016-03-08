package in.twizmwaz.cardinal.module.modules.startTimer;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import in.twizmwaz.cardinal.util.bossBar.BossBars;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class StartTimer implements TaskedModule, Cancellable {

    private int time, originalTime;
    private Match match;
    private boolean cancelled, forced;
    private String bossBar;
    private String neededPlayers;

    public StartTimer(Match match, int ticks) {
        this.time = ticks;
        this.originalTime = ticks;
        this.match = match;
        this.cancelled = true;
        this.bossBar = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.GREEN, BarStyle.SOLID, false);
        this.neededPlayers = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.RED, BarStyle.SOLID, false);
    }

    @Override
    public void unload() {
        BossBars.removeBroadcastedBossBar(bossBar);
        BossBars.removeBroadcastedBossBar(neededPlayers);
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (!isCancelled()) {
            if (time % 20 == 0 && time >= 0 && originalTime != 0) {
                BossBars.setTitle(bossBar, new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTING_IN, time == 20 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.GREEN) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + "" + (time / 20) + "" + ChatColor.GREEN))));
            }
            if ((time % 100 == 0 && time > 0) || (time < 100 && time > 0 && time % 20 == 0)) {
                ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTING_IN, time == 20 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.GREEN) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + "" + (time / 20) + "" + ChatColor.GREEN))));
            }
            BossBars.setProgress(bossBar, (double)time / originalTime);
            if (time == 0) {
                if (match.getState() != MatchState.STARTING) {
                    return;
                } else {
                    int count = 0;
                    for (TeamModule team : Teams.getTeams()) {
                        if (!team.isObserver() && team.size() < team.getMin()) {
                            count++;
                        }
                    }
                    if (count > 0 && !forced) {
                        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_NOT_ENOUGH_PLAYERS)));
                        this.setCancelled(true);
                        return;
                    }
                    BossBars.removeBroadcastedBossBar(bossBar);
                    BossBars.removeBroadcastedBossBar(neededPlayers);
                    match.setState(MatchState.PLAYING);
                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTED)));
                    Bukkit.getServer().getPluginManager().callEvent(new MatchStartEvent());
                }
            }
            if (time <= 60 && time >= 20 && time % 20 == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
                    }
                    if (!Teams.getTeamByPlayer(player).get().isObserver()) {
                        player.showTitle(new TextComponent(ChatColor.YELLOW + "" + (time/20)), new TextComponent(""), 0, 10, 10);
                    }
                }
            }
            if (time == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 2);
                    }
                    if (!Teams.getTeamByPlayer(player).get().isObserver()) {
                        String title = new LocalizedChatMessage(ChatConstant.UI_MATCH_START_TITLE).getMessage(player.getLocale());
                        player.showTitle(new TextComponent(ChatColor.GREEN + title), new TextComponent(""), 0, 10, 10);
                    }
                }
            }
            if (time < 0) {
                setCancelled(true);
            }
            time--;
        }
        if ((GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) && neededPlayers() > 0) {
            BossBars.setTitle(neededPlayers, waitingPlayerMessage());
            BossBars.setVisible(neededPlayers, true);
        } else {
            BossBars.setVisible(neededPlayers, false);
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
        if (this.cancelled && GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            GameHandler.getGameHandler().getMatch().setState(MatchState.WAITING);
            BossBars.setVisible(bossBar, false);
        } else {
            BossBars.setVisible(bossBar, true);
        }
    }

    public void setTime(int time) {
        this.time = time;
        setOriginalTime(time);
    }

    public void setOriginalTime(int time) {
        this.originalTime = time;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public int neededPlayers(){
        int count = 0;
        for (TeamModule teams : Teams.getTeams()) {
            if (!teams.isObserver() && teams.size() < teams.getMin()) {
                count += teams.getMin() - teams.size();
            }
        }
        return count;
    }

    public UnlocalizedChatMessage waitingPlayerMessage(){
        int count = 0;
        TeamModule team = Teams.getTeamById("observers").get();
        for (TeamModule teams : Teams.getTeams()) {
            if (!teams.isObserver() && teams.size() < teams.getMin()) {
                count ++;
                team = teams;
            }
        }
        if (neededPlayers() == 1) {
            return new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.UI_WAITING_PLAYER, ChatColor.AQUA + "" + neededPlayers() + ChatColor.RED, count == 1 ? team.getCompleteName() : ""));
        } else {
            return new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.UI_WAITING_PLAYERS, ChatColor.AQUA + "" + neededPlayers() + ChatColor.RED, count == 1 ? team.getCompleteName() : ""));
        }
    }

}