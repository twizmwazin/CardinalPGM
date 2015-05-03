package in.twizmwaz.cardinal.module.modules.invisibleBlock;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class InvisibleBlockBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<InvisibleBlock> results = new ModuleCollection<>();
        results.add(new InvisibleBlock());
        return results;
    }

}
