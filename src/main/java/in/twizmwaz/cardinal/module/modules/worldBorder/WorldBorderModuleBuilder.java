package in.twizmwaz.cardinal.module.modules.worldBorder;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Strings;
import org.jdom2.Element;

public class WorldBorderModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<WorldBorderModule> load(Match match) {
        ModuleCollection<WorldBorderModule> results = new ModuleCollection<>();
        for (Element borders : match.getDocument().getRootElement().getChildren("world-borders")) {
            for (Element border : borders.getChildren("world-border")) {
                results.add(parseWorldBorder(border, borders));
            }
            for (Element borders2 : borders.getChildren("world-borders")) {
                for (Element border : borders.getChildren("world-border")) {
                    results.add(parseWorldBorder(border, borders2, borders));
                }
            }
        }
        return results;
    }

    public WorldBorderModule parseWorldBorder(Element... elements) {
        String center = Strings.fallback(Parser.getOrderedAttribute("center", elements), "0,0");
        double x = Numbers.parseDouble(center.split(",")[0].trim(), 0),
                z = Numbers.parseDouble(center.split(",")[1].trim(), 0);

        double size = Numbers.parseDouble(Parser.getOrderedAttribute("size", elements), 0);

        FilterModule when = FilterModuleBuilder.getAttributeOrChild("when", elements);

        int after = Strings.timeStringToSeconds(Strings.fallback(Parser.getOrderedAttribute("after", elements), "0s"));
        long duration = Strings.timeStringToSeconds(Strings.fallback(Parser.getOrderedAttribute("duration", elements), "0s"));
        double damage = Numbers.parseDouble(Parser.getOrderedAttribute("damage", elements), 0.2);
        double buffer = Numbers.parseDouble(Parser.getOrderedAttribute("buffer", elements), 5);
        int warningDistance = Numbers.parseInt(Parser.getOrderedAttribute("warning-distance", elements), 5);
        int warningTime = Strings.timeStringToSeconds(Strings.fallback(Parser.getOrderedAttribute("warning-time", elements), "15s"));;

        return new WorldBorderModule(x, z, size, when, after, duration, damage, buffer, warningDistance, warningTime);
    }

}
