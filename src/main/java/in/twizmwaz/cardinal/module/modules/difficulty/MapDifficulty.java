package in.twizmwaz.cardinal.module.modules.difficulty;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Difficulty;


public class MapDifficulty implements Module {

    protected MapDifficulty(Difficulty difficulty) {
        GameHandler.getGameHandler().getMatchWorld().setDifficulty(difficulty);
    }

    @Override
    public void unload() {

    }


}
