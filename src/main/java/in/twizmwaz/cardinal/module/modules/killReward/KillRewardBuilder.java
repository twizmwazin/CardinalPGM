package in.twizmwaz.cardinal.module.modules.killReward;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import org.jdom2.Element;

public class KillRewardBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for (Element element : match.getDocument().getRootElement().getChildren("killreward")) {
            Kit kit = null;
            for (Element kitElement : element.getChildren()) {
                if (!kitElement.getName().equalsIgnoreCase("filter")) {
                    kit = KitBuilder.getKit(kitElement);
                }
            }
            ModuleCollection<FilterModule> filters = new ModuleCollection<>();
            for (Element filter : element.getChildren("filter")) {
                filters.add(FilterModuleBuilder.getFilter(filter.getChildren().get(0)));
            }
            results.add(new KillReward(kit, filters));
        }
        return results;
    }

}
