package in.twizmwaz.cardinal.module.modules.wools;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
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
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Module> result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("wools")) {
            for (Element subElement : element.getChildren("wool")) {
                result.addAll(getWool(subElement, element));
            }
            for (Element child : element.getChildren("wools")) {
                for (Element subChild : child.getChildren("wool")) {
                    result.addAll(getWool(subChild, child, element));
                }
            }

        }
        return result;
    }

    private ModuleCollection<? extends Module> getWool(Element... elements) {
        TeamModule team = Teams.getTeamById(Parser.getOrderedAttribute("team", elements)).orNull();
        DyeColor color = Parser.parseDyeColor(Parser.getOrderedAttribute("color", elements));
        BlockRegion place = RegionModuleBuilder.getRegion(elements[0].getChildren().get(0)).getCenterBlock();
        String name = color == null ? "Wool" : color.name() + " Wool";
        if (Parser.getOrderedAttribute("name", elements) != null) {
            name = Parser.getOrderedAttribute("name", elements);
        }
        String id = Parser.getOrderedAttribute("id", elements);
        if (id == null) {
            id = Parser.getOrderedAttribute("color", elements);
        }
        boolean craftable = Numbers.parseBoolean(Parser.getOrderedAttribute("craftable", elements), true);
        boolean show = Numbers.parseBoolean(Parser.getOrderedAttribute("show", elements), true);
        boolean required = Numbers.parseBoolean(Parser.getOrderedAttribute("required", elements), show);
        String locationAttribute = Parser.getOrderedAttribute("location", elements);
        Vector location = null;
        if (locationAttribute != null) {
            location = new Vector(Numbers.parseDouble(locationAttribute.replaceAll(" ", ",").split(",")[0]), Numbers.parseDouble(locationAttribute.replaceAll(" ", ",").split(",")[1]), Numbers.parseDouble(locationAttribute.replaceAll(" ", ",").split(",")[2]));
        }
        String woolMetric = Parser.getOrderedAttribute("woolproximity-metric", elements);
        Boolean woolHorizontal = Numbers.parseBoolean(Parser.getOrderedAttribute("woolproximity-horizontal", elements), false);
        GameObjectiveProximityHandler woolProximity = new GameObjectiveProximityHandler(location, woolHorizontal, false,
                woolMetric == null ? GameObjectiveProximityHandler.ProximityMetric.CLOSEST_KILL : GameObjectiveProximityHandler.ProximityMetric.getByName(woolMetric));
        String monumentMetric = Parser.getOrderedAttribute("monumentproximity-metric", elements);
        Boolean monumentHorizontal = Numbers.parseBoolean(Parser.getOrderedAttribute("monumentproximity-horizontal", elements), false);
        GameObjectiveProximityHandler monumentProximity = new GameObjectiveProximityHandler(place.getVector(), monumentHorizontal, true,
                monumentMetric == null ? GameObjectiveProximityHandler.ProximityMetric.CLOSEST_BLOCK : GameObjectiveProximityHandler.ProximityMetric.getByName(monumentMetric));
        ModuleCollection<Module> result =  new ModuleCollection<>();
        result.add(woolProximity);
        result.add(monumentProximity);
        result.add(new WoolObjective(team, name, id, color, place, craftable, show, required, woolProximity, monumentProximity));
        return result;
    }

}
