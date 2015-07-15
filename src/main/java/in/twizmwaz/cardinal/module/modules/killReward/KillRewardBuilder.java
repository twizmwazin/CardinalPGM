package in.twizmwaz.cardinal.module.modules.killReward;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import org.jdom2.Element;

public class KillRewardBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<KillReward> load(Match match) {
        ModuleCollection<KillReward> results = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("killreward")) {
            ModuleCollection<Kit> kits = new ModuleCollection<>();
            kits.add(KitBuilder.getKit(element, GameHandler.getGameHandler().getMatch().getDocument(), true));
            ModuleCollection<FilterModule> filters = new ModuleCollection<>();
            for (Element filter : element.getChildren("filter")) {
                filters.add(FilterModuleBuilder.getFilter(filter.getChildren().get(0)));
            }
            results.add(new KillReward(kits, filters));
        }
        return results;
    }

}
