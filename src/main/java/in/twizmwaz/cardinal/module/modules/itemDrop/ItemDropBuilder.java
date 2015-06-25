package in.twizmwaz.cardinal.module.modules.itemDrop;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class ItemDropBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ItemDrop> load(Match match) {
        return new ModuleCollection<>(new ItemDrop());
    }
}
