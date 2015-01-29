package in.twizmwaz.cardinal.module.modules.timeNotifications;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import sun.java2d.loops.Blit;

public class TimeNotifications implements TaskedModule {

    private static int nextTimeMessage;

    protected TimeNotifications() {
        nextTimeMessage = GameHandler.getGameHandler().getMatch().getPriorityTimeLimit();
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
            if (GameHandler.getGameHandler().getMatch().getPriorityTimeLimit() == 0) {
                if (time >= nextTimeMessage) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Time Elapsed: " + ChatColor.GREEN + StringUtils.formatTime(nextTimeMessage));
                    nextTimeMessage += 300;
                }
                return;
            }
            timeRemaining = GameHandler.getGameHandler().getMatch().getPriorityTimeLimit() - time;
            if (nextTimeMessage >= timeRemaining) {
                if (nextTimeMessage <= 5) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.DARK_RED + StringUtils.formatTime(nextTimeMessage));
                    nextTimeMessage--;
                } else if (nextTimeMessage <= 30) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.GOLD + StringUtils.formatTime(nextTimeMessage));
                    nextTimeMessage -= 5;
                } else if (nextTimeMessage <= 60) {
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
