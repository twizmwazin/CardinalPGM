package in.twizmwaz.cardinal.module.modules.destroyable;

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
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class DestroyableObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<DestroyableObjective> load(Match match) {
        ModuleCollection<DestroyableObjective> result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("destroyables")) {
            for (Element subElement : element.getChildren("destroyable")) {
                result.add(getDestroyable(subElement, element));
            }
            for (Element child : element.getChildren("destroyables")) {
                for (Element subChild : child.getChildren("destroyable")) {
                    result.add(getDestroyable(subChild, child, element));
                }
            }
        }
        return result;
    }

    private DestroyableObjective getDestroyable(Element... elements) {
        TeamModule owner = Teams.getTeamById(Parser.getOrderedText(elements)).orNull();
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
        List<Pair<Material, Integer>> materials = new ArrayList<>();
        String materialsAttribute = Parser.getOrderedAttribute("materials", elements);
        if (materialsAttribute != null) {
            if (materialsAttribute.contains(";")) {
                for (String material : materialsAttribute.split(";")) {
                    parseMaterial(material, materials);
                }
            } else {
                parseMaterial(materialsAttribute, materials);
            }
        }
        String completionAttribute = Parser.getOrderedAttribute("completion", elements);
        double completion = completionAttribute == null ? 1.0 : Numbers.parseDouble(completionAttribute.replaceAll("%", "").replaceAll(" ", "")) / 100.0;
        boolean showProgress = Numbers.parseBoolean(Parser.getOrderedAttribute("show-progress", elements), false);
        boolean repairable = Numbers.parseBoolean(Parser.getOrderedAttribute("repairable", elements), false);
        boolean show = Numbers.parseBoolean(Parser.getOrderedAttribute("show", elements), true);
        boolean changesModes = Numbers.parseBoolean(Parser.getOrderedAttribute("show-progress", elements), false);
        return new DestroyableObjective(owner, name, id, new UnionRegion(null, regions), materials, completion, show, changesModes, showProgress, repairable);
    }

    private void parseMaterial(String text, List<Pair<Material, Integer>> materials) {
        if (text.contains(":")) {
            materials.add(new ImmutablePair<>(Material.matchMaterial(text.split(":")[0].trim()), Numbers.parseInt(text.split(":")[1].trim())));
        } else {
            materials.add(new ImmutablePair<>(Material.matchMaterial(text.trim()), 1));
        }
    }

}
