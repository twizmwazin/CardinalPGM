package in.twizmwaz.cardinal.module.modules.mob;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;

public class MobModuleBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection<MobModule> load(Match match) {
        ModuleCollection<MobModule> results = new ModuleCollection<>();
        if (match.getDocument().getRootElement().getChild("mobs") != null) {
            results.add(new MobModule(FilterModuleBuilder.getFilter(match.getDocument().getRootElement().getChild("mobs").getChild("filter"))));
        } else {
            results.add(new MobModule(FilterModuleBuilder.getFilter("deny-all")));
        }
        return results;
    }
}
