package in.twizmwaz.cardinal.module.modules.killReward;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import in.twizmwaz.cardinal.module.modules.kit.KitNode;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.KitItem;
import in.twizmwaz.cardinal.util.Parser;
import org.jdom2.Element;

public class KillRewardBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<KillReward> load(Match match) {
        ModuleCollection<KillReward> results = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("killreward")) {
            KitNode kit = null;
            if (element.getAttributeValue("kit") != null) kit = KitNode.getKitByName(element.getAttributeValue("kit"));
            if (element.getChild("kit") != null) kit = KitBuilder.getKit(element.getChild("kit"));

            KitItem item = null;
            if (element.getChild("item") != null) {
                item = Parser.getKitItem(element.getChild("item"));
            }

            FilterModule filter = FilterModuleBuilder.getFilter("always");
            if (element.getAttributeValue("filter") != null) filter = FilterModuleBuilder.getFilter(element.getAttributeValue("filter"));
            if (element.getChild("filter") != null) filter = FilterModuleBuilder.getFilter(element.getChild("filter"));

            results.add(new KillReward(item, kit, filter));
        }
        return results;
    }

}
