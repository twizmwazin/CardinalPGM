package in.twizmwaz.cardinal.module.modules.killStreakCount;

import java.util.concurrent.Callable;

public class KillStreak implements Callable {

    private final int value;

    protected KillStreak(final int value) {
        this.value = value;
    }

    @Override
    public Object call() throws Exception {
        return value;
    }

}
