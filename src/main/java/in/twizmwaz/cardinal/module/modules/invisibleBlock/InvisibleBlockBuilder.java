package in.twizmwaz.cardinal.module.modules.invisibleBlock;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class InvisibleBlockBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<InvisibleBlock> load(Match match) {
        return new ModuleCollection<>(new InvisibleBlock());
    }

}
