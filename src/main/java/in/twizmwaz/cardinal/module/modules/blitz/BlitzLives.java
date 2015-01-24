package in.twizmwaz.cardinal.module.modules.blitz;

import java.util.concurrent.Callable;

public class BlitzLives implements Callable {

    private final int lives;

    protected  BlitzLives(final int lives){
        this.lives = lives;
    }

    @Override
    public Object call() throws Exception {
        return lives;
    }

}
