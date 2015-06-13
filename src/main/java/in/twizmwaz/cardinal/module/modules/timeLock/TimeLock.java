package in.twizmwaz.cardinal.module.modules.timeLock;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.World;

public class TimeLock implements Module {

    private World matchworld;

    protected TimeLock() {
        matchworld = GameHandler.getGameHandler().getMatchWorld();
        matchworld.setGameRuleValue("doDaylightCycle", "false");
    }

    @Override
    public void unload() {

    }


}
