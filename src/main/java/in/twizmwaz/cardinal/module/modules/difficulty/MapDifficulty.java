package in.twizmwaz.cardinal.module.modules.difficulty;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Difficulty;
import org.bukkit.event.HandlerList;


public class MapDifficulty implements Module {

    protected MapDifficulty(Difficulty difficulty) {
        GameHandler.getGameHandler().getMatchWorld().setDifficulty(difficulty);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

}
