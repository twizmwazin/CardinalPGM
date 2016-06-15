package in.twizmwaz.cardinal.module.modules.killReward;

import com.google.common.collect.Lists;
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

import java.util.List;

public class KillRewardBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<KillReward> load(Match match) {
        ModuleCollection<KillReward> results = new ModuleCollection<>();
        results.addAll(getKillRewardsIn(match.getDocument().getRootElement()));
        return results;
    }

    private ModuleCollection<KillReward> getKillRewardsIn(Element... elements) {
        ModuleCollection<KillReward> results = new ModuleCollection<>();

        for (Element killReward : Parser.getJoinedElements(elements[0].getChildren("kill-reward"), elements[0].getChildren("killreward"))) {
            results.add(getKillReward(killReward));
        }
        for (Element killRewards : Parser.getJoinedElements(elements[0].getChildren("kill-rewards"), elements[0].getChildren("killrewards"))) {
            results.addAll(getKillRewardsIn(Parser.addElement(killRewards, elements)));
        }

        return results;
    }

    private KillReward getKillReward(Element... elements) {
        KitNode kit = null;
        if (Parser.getOrderedAttribute("kit", elements) != null) kit = KitNode.getKitByName(Parser.getOrderedAttribute("kit", elements));
        if (elements[0].getChild("kit") != null) kit = KitBuilder.getKit(elements[0].getChild("kit"));

        List<KitItem> items = Lists.newArrayList();
        for (Element item : elements[0].getChildren("item")) {
            items.add(Parser.getKitItem(item));
        }

        FilterModule filter = FilterModuleBuilder.getAttributeOrChild("filter", "always", elements);

        return new KillReward(items, kit, filter);
    }

}
