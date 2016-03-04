package in.twizmwaz.cardinal.module.modules.longTntRender;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class LongTntRenderBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<LongTntRender> load(Match match) {
        return new ModuleCollection<>(new LongTntRender());
    }

}
