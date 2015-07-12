package in.twizmwaz.cardinal.module.modules.timeNotifications;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Strings;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;

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
