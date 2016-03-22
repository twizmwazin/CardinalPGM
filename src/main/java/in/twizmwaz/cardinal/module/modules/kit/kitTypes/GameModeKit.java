package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class GameModeKit implements Kit {

    private GameMode gameMode;

    public GameModeKit(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        player.setGameMode(gameMode);
    }

}
