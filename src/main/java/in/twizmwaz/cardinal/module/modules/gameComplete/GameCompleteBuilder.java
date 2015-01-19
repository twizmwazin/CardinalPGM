package in.twizmwaz.cardinal.module.modules.gameComplete;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

public class GameCompleteBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        results.add(new GameComplete());
        return results;
    }

}
