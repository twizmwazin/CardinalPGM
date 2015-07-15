package in.twizmwaz.cardinal.module.modules.gameComplete;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class GameCompleteBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<GameComplete> load(Match match) {
        return new ModuleCollection<>(new GameComplete());
    }

}
