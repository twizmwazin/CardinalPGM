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
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Material;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class CoreObjectiveBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("cores")) {
            for (Element subElement : element.getChildren("core")) {
                TeamModule team;
                try {
                    team = Teams.getTeamById(subElement.getAttributeValue("team")).orNull();
                } catch (NullPointerException e) {
                    team = Teams.getTeamById(element.getAttributeValue("team")).orNull();
                }
                String name = "Core";
                if (subElement.getAttributeValue("name") != null) {
                    name = subElement.getAttributeValue("name");
                } else if (element.getAttributeValue("name") != null) {
                    name = element.getAttributeValue("name");
                }
                String id = null;
                if (subElement.getAttributeValue("id") != null) {
                    id = subElement.getAttributeValue("id");
                } else if (element.getAttributeValue("id") != null) {
                    id = element.getAttributeValue("id");
                }
                ModuleCollection<RegionModule> regions = new ModuleCollection<>();
                if (subElement.getAttributeValue("region") != null) {
                    regions.add(RegionModuleBuilder.getRegion(subElement.getAttributeValue("region")));
                } else {
                    for (Element region : subElement.getChildren()) {
                        regions.add(RegionModuleBuilder.getRegion(region));
                    }
                }
                int leak = 5;
                if (subElement.getAttributeValue("leak") != null) {
                    leak = Numbers.parseInt(subElement.getAttributeValue("leak").replaceAll(" ", ""));
                } else if (element.getAttributeValue("leak") != null) {
                    leak = Numbers.parseInt(element.getAttributeValue("leak").replaceAll(" ", ""));
                }
                Material type = Material.OBSIDIAN;
                int damageValue = -1;
                if (subElement.getAttributeValue("material") != null) {
                    String material = subElement.getAttributeValue("material");
                    if (material.contains(":")) {
                        type = Material.matchMaterial(material.split(":")[0].trim());
                        damageValue = Numbers.parseInt(material.split(":")[1].trim());
                    } else {
                        type = Material.matchMaterial(material.trim());
                    }
                } else if (element.getAttributeValue("material") != null) {
                    String material = element.getAttributeValue("material");
                    if (material.contains(":")) {
                        type = Material.matchMaterial(material.split(":")[0].trim());
                        damageValue = Numbers.parseInt(material.split(":")[1].trim());
                    } else {
                        type = Material.matchMaterial(material.trim());
                    }
                }
                boolean show = true;
                if (subElement.getAttributeValue("show") != null) {
                    show = !subElement.getAttributeValue("show").equalsIgnoreCase("false");
                } else if (element.getAttributeValue("show") != null) {
                    show = !element.getAttributeValue("show").equalsIgnoreCase("false");
                }
                boolean changesModes = false;
                if (subElement.getAttributeValue("mode-changes") != null) {
                    changesModes = subElement.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                } else if (element.getAttributeValue("mode-changes") != null) {
                    changesModes = element.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                }
                result.add(new CoreObjective(team, name, id, new UnionRegion(null, regions), leak, type, damageValue, show, changesModes));
            }
            for (Element child : element.getChildren("cores")) {
                for (Element subChild : child.getChildren("core")) {
                    TeamModule team;
                    try {
                        team = Teams.getTeamById(subChild.getAttributeValue("team")).orNull();
                    } catch (NullPointerException e) {
                        try {
                            team = Teams.getTeamById(child.getAttributeValue("team")).orNull();
                        } catch (NullPointerException ex) {
                            team = Teams.getTeamById(element.getAttributeValue("team")).orNull();
                        }
                    }
                    String name = "Core";
                    if (subChild.getAttributeValue("name") != null) {
                        name = subChild.getAttributeValue("name");
                    } else if (child.getAttributeValue("name") != null) {
                        name = child.getAttributeValue("name");
                    } else if (element.getAttributeValue("name") != null) {
                        name = element.getAttributeValue("name");
                    }
                    String id = null;
                    if (subChild.getAttributeValue("id") != null) {
                        id = subChild.getAttributeValue("id");
                    } else if (child.getAttributeValue("id") != null) {
                        id = child.getAttributeValue("id");
                    } else if (element.getAttributeValue("id") != null) {
                        id = element.getAttributeValue("id");
                    }
                    ModuleCollection<RegionModule> regions = new ModuleCollection<>();
                    if (subChild.getAttributeValue("region") != null) {
                        regions.add(RegionModuleBuilder.getRegion(subChild));
                    } else {
                        for (Element region : subChild.getChildren()) {
                            regions.add(RegionModuleBuilder.getRegion(region));
                        }
                    }
                    int leak = 5;
                    if (subChild.getAttributeValue("leak") != null) {
                        leak = Numbers.parseInt(subChild.getAttributeValue("leak").replaceAll(" ", ""));
                    } else if (child.getAttributeValue("leak") != null) {
                        leak = Numbers.parseInt(child.getAttributeValue("leak").replaceAll(" ", ""));
                    } else if (element.getAttributeValue("leak") != null) {
                        leak = Numbers.parseInt(element.getAttributeValue("leak").replaceAll(" ", ""));
                    }
                    Material type = Material.OBSIDIAN;
                    int damageValue = -1;
                    if (subChild.getAttributeValue("material") != null) {
                        String material = subChild.getAttributeValue("material");
                        if (material.contains(":")) {
                            type = Material.matchMaterial(material.split(":")[0].trim());
                            damageValue = Numbers.parseInt(material.split(":")[1].trim());
                        } else {
                            type = Material.matchMaterial(material.trim());
                        }
                    } else if (child.getAttributeValue("material") != null) {
                        String material = child.getAttributeValue("material");
                        if (material.contains(":")) {
                            type = Material.matchMaterial(material.split(":")[0].trim());
                            damageValue = Numbers.parseInt(material.split(":")[1].trim());
                        } else {
                            type = Material.matchMaterial(material.trim());
                        }
                    } else if (element.getAttributeValue("material") != null) {
                        String material = element.getAttributeValue("material");
                        if (material.contains(":")) {
                            type = Material.matchMaterial(material.split(":")[0].trim());
                            damageValue = Numbers.parseInt(material.split(":")[1].trim());
                        } else {
                            type = Material.matchMaterial(material.trim());
                        }
                    }
                    boolean show = true;
                    if (subChild.getAttributeValue("show") != null) {
                        show = !subChild.getAttributeValue("show").equalsIgnoreCase("false");
                    } else if (child.getAttributeValue("show") != null) {
                        show = !child.getAttributeValue("show").equalsIgnoreCase("false");
                    } else if (element.getAttributeValue("show") != null) {
                        show = !element.getAttributeValue("show").equalsIgnoreCase("false");
                    }
                    boolean changesModes = false;
                    if (subChild.getAttributeValue("mode-changes") != null) {
                        changesModes = subChild.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                    } else if (child.getAttributeValue("mode-changes") != null) {
                        changesModes = child.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                    } else if (element.getAttributeValue("mode-changes") != null) {
                        changesModes = element.getAttributeValue("mode-changes").equalsIgnoreCase("true");
                    }
                    result.add(new CoreObjective(team, name, id, new UnionRegion(null, regions), leak, type, damageValue, show, changesModes));
                }
            }
        }
        return result;
    }

}
