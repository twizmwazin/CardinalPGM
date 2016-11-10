package in.twizmwaz.cardinal.module.modules.timeNotifications;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.bossBar.BossBars;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class TimeNotifications implements TaskedModule {

    public UUID bossBar;
    private int lastSecond = 0;

    protected TimeNotifications() {
        this.bossBar = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.GREEN, BarStyle.SOLID, false);
    }

    public void changeTime(int timeLimit) {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            lastSecond = 0;
            BossBars.setVisible(bossBar, timeLimit > 0);
            if (timeLimit <= 0) sendTimeElapsedMessage(MatchTimer.getTimeInSeconds());
        }
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        if (TimeLimit.getMatchTimeLimit() > 0) {
            int timeRemaining = TimeLimit.getMatchTimeLimit();
            BossBars.setTitle(bossBar, new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatUtil.getTimerColor(timeRemaining) + "{1}", new LocalizedChatMessage(ChatConstant.UI_TIMER), new UnlocalizedChatMessage(Strings.formatTime(timeRemaining))));
            BossBars.setVisible(bossBar, true);
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        BossBars.removeBroadcastedBossBar(bossBar);
    }

    @Override
    public void unload() {
        BossBars.removeBroadcastedBossBar(bossBar);
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            int timeLimit = TimeLimit.getMatchTimeLimit();
            double time = MatchTimer.getTimeInSeconds();
            if (timeLimit > 0) {
                double timeRemaining = timeLimit - time;
                BossBars.setProgress(bossBar, timeRemaining / timeLimit);
                if (lastSecond != (int) time) {
                    lastSecond = (int) time;
                    BossBars.setTitle(bossBar, new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatUtil.getTimerColor(timeRemaining) + "{1}", new LocalizedChatMessage(ChatConstant.UI_TIMER), new UnlocalizedChatMessage(Strings.formatTime(timeRemaining + 1))));
                    if (timeRemaining < 30) {
                        BossBars.broadcastedBossBars.get(bossBar).setColor(BarColor.RED);
                    } else if (timeRemaining < 60) {
                        BossBars.broadcastedBossBars.get(bossBar).setColor(BarColor.YELLOW);
                    } else {
                        BossBars.broadcastedBossBars.get(bossBar).setColor(BarColor.GREEN);
                    }
                }
            } else if (lastSecond != (int) time) {
                lastSecond = (int) time;
                if ((int)time % 300 == 0) sendTimeElapsedMessage(time);
            }
        }
    }

    private void sendTimeElapsedMessage(double time) {
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_TIME_ELAPSED, new UnlocalizedChatMessage(ChatColor.GREEN + Strings.formatTime(time)))));
    }

}