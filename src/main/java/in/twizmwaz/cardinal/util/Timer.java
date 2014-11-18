package in.twizmwaz.cardinal.util;

/**
 * Created by kevin on 11/17/14.
 */
public class Timer implements Runnable {

    private int time;

    public Timer() {}

    public void setTime(int seconds) {
        this.time = seconds;
    }

    public int getTime() {
        return time;
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
