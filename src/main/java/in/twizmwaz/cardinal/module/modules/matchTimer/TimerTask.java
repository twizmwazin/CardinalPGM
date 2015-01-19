package in.twizmwaz.cardinal.module.modules.matchTimer;

import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.Bukkit;

public class TimerTask implements Runnable {

    private int time;
    private static int matchTime;

    public TimerTask(int time) {
        this.time = time;
        matchTime = time;
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GameHandler.getGameHandler().getPlugin(), new TimerTask(time ++));
        }
    }

    public static int getMatchTime() {
        return matchTime;
    }
}
