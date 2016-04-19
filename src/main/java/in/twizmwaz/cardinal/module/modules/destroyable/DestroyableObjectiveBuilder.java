package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
import in.twizmwaz.cardinal.module.modules.proximity.ProximityInfo;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class DestroyableObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Module> result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("destroyables")) {
            for (Element subElement : element.getChildren("destroyable")) {
                result.addAll(getDestroyable(subElement, element));
            }
            for (Element child : element.getChildren("destroyables")) {
                for (Element subChild : child.getChildren("destroyable")) {
                    result.addAll(getDestroyable(subChild, child, element));
                }
            }
        }
        return result;
    }

    private ModuleCollection<? extends Module> getDestroyable(Element... elements) {
        TeamModule owner = Teams.getTeamById(Parser.getOrderedAttribute("owner", elements)).orNull();
        String name = Parser.getOrderedAttribute("name", elements);
        if (name == null) {
            name = "Monument";
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
        List<Pair<Material, Integer>> materials = new ArrayList<>();
        String materialsAttribute = Parser.getOrderedAttribute("materials", elements);
        if (materialsAttribute != null) {
            if (materialsAttribute.contains(";")) {
                for (String material : materialsAttribute.split(";")) {
                    materials.add(Parser.parseMaterial(material));
                }
            } else {
                materials.add(Parser.parseMaterial(materialsAttribute));
            }
        }
        String completionAttribute = Parser.getOrderedAttribute("completion", elements);
        double completion = completionAttribute == null ? 1.0 : Numbers.parseDouble(completionAttribute.replaceAll("%", "").replaceAll(" ", "")) / 100.0;
        boolean showProgress = Numbers.parseBoolean(Parser.getOrderedAttribute("show-progress", elements), false);
        boolean repairable = Numbers.parseBoolean(Parser.getOrderedAttribute("repairable", elements), false);
        boolean show = Numbers.parseBoolean(Parser.getOrderedAttribute("show", elements), true);
        boolean required = Numbers.parseBoolean(Parser.getOrderedAttribute("required", elements), show);
        boolean changesModes = Numbers.parseBoolean(Parser.getOrderedAttribute("mode-changes", elements), false);
        String proximityMetric = Parser.getOrderedAttribute("proximity-metric", elements);
        Boolean proximityHorizontal = Numbers.parseBoolean(Parser.getOrderedAttribute("proximity-horizontal", elements), false);
        ProximityInfo proximityInfo = new ProximityInfo(region.getCenterBlock().getVector(), proximityHorizontal, false,
                proximityMetric == null ? GameObjectiveProximityHandler.ProximityMetric.CLOSEST_PLAYER : GameObjectiveProximityHandler.ProximityMetric.getByName(proximityMetric));
        Map<String, GameObjectiveProximityHandler> proximityHandlers = new HashMap<>();
        ModuleCollection<Module> result =  new ModuleCollection<>();
        for (TeamModule offender : Teams.getTeams()) {
            if (offender.isObserver() || offender.equals(owner)) continue;
            GameObjectiveProximityHandler proximityHandler = new GameObjectiveProximityHandler(offender, proximityInfo);
            proximityHandlers.put(offender.getId(), proximityHandler);
            result.add(proximityHandler);
        }
        result.add(new DestroyableObjective(owner, name, id, region, materials, completion, show, required, changesModes, showProgress, repairable, proximityHandlers));
        return result;
    }

}
