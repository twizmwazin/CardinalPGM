package in.twizmwaz.cardinal.module.modules.cores;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
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
    public ModuleCollection<CoreObjective> load(Match match) {
        ModuleCollection<CoreObjective> result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("cores")) {
            for (Element subElement : element.getChildren("core")) {
                result.add(getCore(subElement, element));
            }
            for (Element child : element.getChildren("cores")) {
                for (Element subChild : child.getChildren("core")) {
                    result.add(getCore(subChild, child, element));
                }
            }
        }
        return result;
    }

    private CoreObjective getCore(Element... elements) {
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
        int leak = Numbers.parseInt(Parser.getOrderedAttribute("leak", elements), 5);
        ImmutablePair<Material, Integer> material = new ImmutablePair<>(Material.OBSIDIAN, -1);
        String materialAttribute = Parser.getOrderedAttribute("material", elements);
        if (materialAttribute != null) {
            if (materialAttribute.contains(":")) {
                material = new ImmutablePair<>(Material.matchMaterial(materialAttribute.split(":")[0].trim()), Numbers.parseInt(materialAttribute.split(":")[1].trim()));
            } else {
                material = new ImmutablePair<>(Material.matchMaterial(materialAttribute.trim()), -1);
            }
        }
        boolean show = Numbers.parseBoolean(Parser.getOrderedAttribute("show"), true);
        boolean modeChanges = Numbers.parseBoolean(Parser.getOrderedAttribute("mode-changes"), false);
        return new CoreObjective(team, name, id, new UnionRegion(null, regions), leak, material, show, modeChanges);
    }

}
