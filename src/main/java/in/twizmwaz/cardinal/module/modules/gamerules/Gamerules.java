package in.twizmwaz.cardinal.module.modules.gamerules;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.World;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class Gamerules implements Module {

    private final Set<String> toDisable;
    private World matchworld;

    protected Gamerules(final Set<String> toDisable) {
        this.toDisable = toDisable;
        matchworld = GameHandler.getGameHandler().getMatchWorld();
        for (String gamerule : toDisable) {
            matchworld.setGameRuleValue(gamerule, "false");
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
