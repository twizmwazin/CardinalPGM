package in.twizmwaz.cardinal.module.modules.wools;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.DyeColor;
import org.bukkit.util.Vector;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class WoolObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<WoolObjective> load(Match match) {
        ModuleCollection<WoolObjective> result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("wools")) {
            for (Element subElement : element.getChildren("wool")) {
                result.add(getWool(subElement, element));
            }
            for (Element child : element.getChildren("wools")) {
                for (Element subChild : child.getChildren("wool")) {
                    result.add(getWool(subChild, child, element));
                }
            }

        }
        return result;
    }

    private WoolObjective getWool(Element... elements) {
        String id = Parser.getOrderedAttribute("id", elements);
        boolean required = Numbers.parseBoolean(Parser.getOrderedAttribute("required", elements), true);
        TeamModule team = Teams.getTeamById(Parser.getOrderedAttribute("team", elements)).orNull();
        DyeColor color = Parser.parseDyeColor(Parser.getOrderedAttribute("color", elements));
        if (id == null) {
            id = Parser.getOrderedAttribute("color", elements);
        }
        BlockRegion monument = null;
        String monumentAttribute = Parser.getOrderedAttribute("monument", elements);
        if (elements[0].getChildren().size() > 0) {
            monument = RegionModuleBuilder.getRegion(elements[0].getChildren().get(0)).getCenterBlock();
        } else if (monumentAttribute != null) {
            RegionModule region = RegionModuleBuilder.getRegion(monumentAttribute);
            if (region != null) {
                monument = region.getCenterBlock();
            }
        }
        boolean craftable = Numbers.parseBoolean(Parser.getOrderedAttribute("craftable", elements), true);
        boolean show = Numbers.parseBoolean(Parser.getOrderedAttribute("show", elements), true);
        Vector location = null;
        String locationAttribute = Parser.getOrderedAttribute("location");
        if (locationAttribute != null) {
            location = new Vector(Numbers.parseDouble(locationAttribute.replaceAll(" ", ",").split(",")[0]), Numbers.parseDouble(locationAttribute.replaceAll(" ", ",").split(",")[1]), Numbers.parseDouble(locationAttribute.replaceAll(" ", ",").split(",")[2]));
        }
        GameObjective.ProximityMetric woolProximityMetric = GameObjective.ProximityMetric.matchProximityMetric(Parser.getOrderedAttribute("woolproximity-metric"));
        if (woolProximityMetric == null) {
            woolProximityMetric = GameObjective.ProximityMetric.CLOSEST_KILL;
        }
        boolean woolProximityHorizontal = Numbers.parseBoolean(Parser.getOrderedAttribute("woolproximity-horizontal"), false);
        GameObjective.ProximityMetric monumentProximityMetric = GameObjective.ProximityMetric.matchProximityMetric(Parser.getOrderedAttribute("monumentproximity-metric"));
        if (monumentProximityMetric == null) {
            monumentProximityMetric = GameObjective.ProximityMetric.CLOSEST_BLOCK;
        }
        boolean monumentProximityHorizontal = Numbers.parseBoolean(Parser.getOrderedAttribute("monumentproximity-horizontal"), false);
        return new WoolObjective(id, required, team, color, monument, craftable, show, location, woolProximityMetric, woolProximityHorizontal, monumentProximityMetric, monumentProximityHorizontal);
    }

}
