package in.twizmwaz.cardinal.module.modules.cores;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.Material;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class CoreObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Module> result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("cores")) {
            for (Element subElement : element.getChildren("core")) {
                result.addAll(getCore(subElement, element));
            }
            for (Element child : element.getChildren("cores")) {
                for (Element subChild : child.getChildren("core")) {
                    result.addAll(getCore(subChild, child, element));
                }
            }
        }
        return result;
    }

    private ModuleCollection<? extends Module> getCore(Element... elements) {
        TeamModule team = Teams.getTeamById(Parser.getOrderedAttribute("team", elements)).orNull();
        String name = Parser.getOrderedAttribute("name", elements);
        if (name == null) {
            name = "Core";
        }
        String id = Parser.getOrderedAttribute("id", elements);
        ModuleCollection<RegionModule> regions = new ModuleCollection<>();
        if (elements[0].getAttributeValue("region") != null) {
            regions.add(RegionModuleBuilder.getRegion(elements[0].getAttributeValue("region")));
        } else {
            for (Element region : elements[0].getChildren()) {
                regions.add(RegionModuleBuilder.getRegion(region));
            }
        }
        RegionModule region = new UnionRegion(null, regions);
        int leak = Numbers.parseInt(Parser.getOrderedAttribute("leak", elements), 5);
        ImmutablePair<Material, Integer> material = new ImmutablePair<>(Material.OBSIDIAN, -1);
        String materialAttribute = Parser.getOrderedAttribute("material", elements);
        if (materialAttribute != null) {
            material = (ImmutablePair<Material, Integer>) Parser.parseMaterial(materialAttribute);
        }
        boolean show = Numbers.parseBoolean(Parser.getOrderedAttribute("show", elements), true);
        boolean required = Numbers.parseBoolean(Parser.getOrderedAttribute("required", elements), show);
        boolean modeChanges = Numbers.parseBoolean(Parser.getOrderedAttribute("mode-changes", elements), false);
        String proximityMetric = Parser.getOrderedAttribute("proximity-metric", elements);
        Boolean proximityHorizontal = Numbers.parseBoolean(Parser.getOrderedAttribute("proximity-horizontal", elements), false);
        GameObjectiveProximityHandler proximity = new GameObjectiveProximityHandler(region.getCenterBlock().getVector(), proximityHorizontal, false,
                proximityMetric == null ? GameObjectiveProximityHandler.ProximityMetric.CLOSEST_PLAYER : GameObjectiveProximityHandler.ProximityMetric.getByName(proximityMetric));
        ModuleCollection<Module> result =  new ModuleCollection<>();
        result.add(proximity);
        result.add(new CoreObjective(team, name, id, region, leak, material, show, required, modeChanges, proximity));
        return result;
    }

}
