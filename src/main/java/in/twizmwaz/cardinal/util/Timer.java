package in.twizmwaz.cardinal.util;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by kevin on 11/17/14.
 */
public class Timer extends BukkitRunnable {

    private int time;

    public Timer() {
    }

    public int getTime() {
        return time;
    }

    public void setTime(int seconds) {
        this.time = seconds;
    }

    @Override
    public void run() {
        if (this.time > 0) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time--;
            run();

        }
    }
}
