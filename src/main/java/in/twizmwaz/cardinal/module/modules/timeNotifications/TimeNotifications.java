package in.twizmwaz.cardinal.module.modules.timeNotifications;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.bossBar.BossBar;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.monumentModes.MonumentModes;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Strings;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.List;

public class TimeNotifications implements TaskedModule {

    private static int nextTimeMessage;

    protected TimeNotifications() {
        nextTimeMessage = TimeLimit.getMatchTimeLimit();
    }

    public static void resetNextMessage() {
        if (TimeLimit.getMatchTimeLimit() == 0) {
            nextTimeMessage = (int) Math.round(MatchTimer.getTimeInSeconds());
        } else {
            nextTimeMessage = (int) Math.round(TimeLimit.getMatchTimeLimit() - MatchTimer.getTimeInSeconds());
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            double time = MatchTimer.getTimeInSeconds();
            double timeRemaining;
            if (TimeLimit.getMatchTimeLimit() == 0) {
                if (time >= nextTimeMessage) {
                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_TIME_ELAPSED, new UnlocalizedChatMessage(ChatColor.GREEN + Strings.formatTime(nextTimeMessage)))));
                    nextTimeMessage += 300;
                }
                return;
            }
            timeRemaining = TimeLimit.getMatchTimeLimit() - time;
            if (GameHandler.getGameHandler().getMatch().getModules().getModule(MonumentModes.class) != null) {
                ModuleCollection<MonumentModes> modes = GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class);
                HashMap<MonumentModes, Integer> modesWithTime = new HashMap<>();
                for (MonumentModes modeForTime : modes) {
                    modesWithTime.put(modeForTime, modeForTime.getTimeAfter());
                }
                List<MonumentModes> sortedModes = MiscUtil.getSortedHashMapKeyset(modesWithTime);
                int timeBeforeMode = 1;
                int showBefore = 60;
                String name = MonumentModes.getModeName();
                for (MonumentModes mode : sortedModes) {
                    if (!mode.hasRan()) {
                        timeBeforeMode = mode.getTimeAfter() - (int) MatchTimer.getTimeInSeconds();
                        name = mode.getName();
                        showBefore = mode.getShowBefore();
                    }
                }
                int timeLeft = TimeLimit.getMatchTimeLimit() - (int) MatchTimer.getTimeInSeconds();
                if (((timeBeforeMode > showBefore) || (name == null)) && (TimeLimit.getMatchTimeLimit() > 0)) {
                    int percent = (int) ((100F * timeLeft) / TimeLimit.getMatchTimeLimit());
                    BossBar.sendGlobalBossBar(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatUtil.getTimerColor(timeRemaining) + "{1}", new LocalizedChatMessage(ChatConstant.UI_TIMER), new UnlocalizedChatMessage(Strings.formatTime(timeRemaining + 1))), percent);
                }
                if (timeBeforeMode == showBefore || timeBeforeMode <= 0) {
                    BossBar.delete();
                }
            } else if (TimeLimit.getMatchTimeLimit() > 0) {
                int timeLeft = ((TimeLimit.getMatchTimeLimit() - (int) MatchTimer.getTimeInSeconds()));
                int percent = (int) ((100F * timeLeft) / TimeLimit.getMatchTimeLimit());
                BossBar.sendGlobalBossBar(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatUtil.getTimerColor(timeRemaining) + "{1}", new LocalizedChatMessage(ChatConstant.UI_TIMER), new UnlocalizedChatMessage(Strings.formatTime(timeRemaining + 1))), percent);
            }
            if (nextTimeMessage >= timeRemaining) {
                if (nextTimeMessage <= 5) {
                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatColor.DARK_RED + Strings.formatTime(nextTimeMessage), new LocalizedChatMessage(ChatConstant.UI_TIMER)));
                    nextTimeMessage--;
                } else if (nextTimeMessage <= 30) {
                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatColor.GOLD + Strings.formatTime(nextTimeMessage), new LocalizedChatMessage(ChatConstant.UI_TIMER)));
                    nextTimeMessage -= 5;
                } else if (nextTimeMessage <= 60) {
                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatColor.YELLOW + Strings.formatTime(nextTimeMessage), new LocalizedChatMessage(ChatConstant.UI_TIMER)));
                    nextTimeMessage -= 15;
                } else {
                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatColor.GREEN + Strings.formatTime(nextTimeMessage), new LocalizedChatMessage(ChatConstant.UI_TIMER)));
                    if ((nextTimeMessage / 60) % 5 == 0 && nextTimeMessage != 300) {
                        nextTimeMessage -= 300;
                    } else if (nextTimeMessage % 60 == 0 && nextTimeMessage <= 300) {
                        nextTimeMessage -= 60;
                    } else {
                        nextTimeMessage = (nextTimeMessage / 300) * 300;
                    }
                }
            }
        }
    }

}