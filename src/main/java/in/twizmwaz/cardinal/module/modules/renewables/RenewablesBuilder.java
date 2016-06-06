package in.twizmwaz.cardinal.module.modules.renewables;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.filter.parsers.BlockFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.type.BlockFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.logic.AnyFilter;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Strings;
import org.jdom2.Element;

public class RenewablesBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Renewable> load(Match match) {
        ModuleCollection<Renewable> results = new ModuleCollection<>();
        for (Element renewables : match.getDocument().getRootElement().getChildren("renewables")) {
            for (Element renewable : renewables.getChildren("renewable")) {
                results.add(getRenewable(renewable, renewables));
            }
            for (Element renewables2 : renewables.getChildren("renewables")) {
                for (Element renewable : renewables2.getChildren("renewable")) {
                    results.add(getRenewable(renewable, renewables2, renewable));
                }
            }
        }
        return results;
    }

    private Renewable getRenewable(Element... elements) {
        RegionModule region = RegionModuleBuilder.getAttributeOrChild("region", elements);

        FilterModule renewFilter = getFilterOrCreate("renew-filter", "renew", "always", elements);
        FilterModule replaceFilter = getFilterOrCreate("replace-filter", "replace", "always", elements);
        FilterModule shuffleFilter = getFilterOrCreate("shuffle-filter", "shuffle", "never", elements);

        double rate = Numbers.parseDouble(Parser.getOrderedAttribute("rate", elements), 1);
        double interval = Strings.timeStringToExactSeconds(Strings.fallback(Parser.getOrderedAttribute("interval", elements), "-1s"));

        boolean grow = Numbers.parseBoolean(Parser.getOrderedAttribute("grow", elements), true);
        boolean particles = Numbers.parseBoolean(Parser.getOrderedAttribute("particles", elements), true);
        boolean sound = Numbers.parseBoolean(Parser.getOrderedAttribute("sound", elements), true);
        int avoidPlayers = Numbers.parseInt(Parser.getOrderedAttribute("avoid-players", elements), 2);

        return new Renewable(region, renewFilter, replaceFilter, shuffleFilter, rate, interval, grow, particles, sound, avoidPlayers);
    }

    private FilterModule getFilterOrCreate(String filter, String child, String fallback, Element... elements) {
        FilterModule filterModule = FilterModuleBuilder.getAttributeOrChild(filter, elements);
        if (filterModule == null) {
            if (elements[0].getChildren(child).size() > 0) {
                ModuleCollection<FilterModule> children = new ModuleCollection<>();
                for (Element element : elements[0].getChildren(child)) {
                    children.add(new BlockFilter(new BlockFilterParser(element)));
                }
                filterModule = new AnyFilter(null, children);
            } else {
                filterModule = FilterModuleBuilder.getFilter(fallback);
            }
        }
        return filterModule;
    }


}
