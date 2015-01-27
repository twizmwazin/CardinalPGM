package in.twizmwaz.cardinal.module.modules.timeNotifications;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;

public class TimeNotifications implements TaskedModule {

    private static int nextTimeMessage;

    protected TimeNotifications() {
        nextTimeMessage = ScoreModule.getTimeLimit();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            double time = MatchTimer.getTimeInSeconds();
            if (ScoreModule.getTimeLimit() == 0) {
                if (time >= nextTimeMessage) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Time Elapsed: " + ChatColor.GREEN + StringUtils.formatTime(nextTimeMessage));
                    nextTimeMessage += 300;
                }
            } else {
                double timeRemaining = ScoreModule.getTimeLimit() - time;
                if (nextTimeMessage >= timeRemaining) {
                    if (timeRemaining <= 5) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.DARK_RED + StringUtils.formatTime(nextTimeMessage));
                        nextTimeMessage--;
                    } else if (timeRemaining <= 30) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.GOLD + StringUtils.formatTime(nextTimeMessage));
                        nextTimeMessage -= 5;
                    } else if (timeRemaining <= 60) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.YELLOW + StringUtils.formatTime(nextTimeMessage));
                        nextTimeMessage -= 15;
                    } else {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.GREEN + StringUtils.formatTime(nextTimeMessage));
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
}
