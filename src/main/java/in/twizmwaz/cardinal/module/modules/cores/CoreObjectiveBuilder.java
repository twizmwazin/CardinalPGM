package in.twizmwaz.cardinal.module.modules.cores;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class CoreObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("cores")) {
            for (Element subElement : element.getChildren("core")) {
                TeamModule team;
                try {
                    team = TeamUtils.getTeamById(subElement.getAttributeValue("team"));
                } catch (NullPointerException e) {
                    team = TeamUtils.getTeamById(element.getAttributeValue("team"));
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
                List<Region> regions = new ArrayList<>();
                if (subElement.getAttributeValue("region") != null) {
                    regions.add(Region.getRegion(subElement));
                } else {
                    for (Element region : subElement.getChildren()) {
                        regions.add(Region.getRegion(region));
                    }
                }
                int leak = 5;
                if (subElement.getAttributeValue("leak") != null) {
                    leak = Integer.parseInt(subElement.getAttributeValue("leak").replaceAll(" ", ""));
                } else if (element.getAttributeValue("leak") != null) {
                    leak = Integer.parseInt(element.getAttributeValue("leak").replaceAll(" ", ""));
                }
                Material type = Material.OBSIDIAN;
                int damageValue = 0;
                if (subElement.getAttributeValue("material") != null) {
                    String material = subElement.getAttributeValue("material");
                    if (material.contains(":")) {
                        type = Material.matchMaterial(material.split(":")[0].trim());
                        damageValue = Integer.parseInt(material.split(":")[1].trim());
                    } else {
                        type = Material.matchMaterial(material.trim());
                    }
                } else if (element.getAttributeValue("material") != null) {
                    String material = element.getAttributeValue("material");
                    if (material.contains(":")) {
                        type = Material.matchMaterial(material.split(":")[0].trim());
                        damageValue = Integer.parseInt(material.split(":")[1].trim());
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
                result.add(new CoreObjective(team, name, id, new UnionRegion(regions), leak, type, damageValue, show));
            }
            for (Element child : element.getChildren("cores")) {
                for (Element subChild : child.getChildren("core")) {
                    TeamModule team;
                    try {
                        team = TeamUtils.getTeamById(subChild.getAttributeValue("team"));
                    } catch (NullPointerException e) {
                        try {
                            team = TeamUtils.getTeamById(child.getAttributeValue("team"));
                        } catch (NullPointerException ex) {
                            team = TeamUtils.getTeamById(element.getAttributeValue("team"));
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
                    List<Region> regions = new ArrayList<>();
                    if (subChild.getAttributeValue("region") != null) {
                        regions.add(Region.getRegion(subChild));
                    } else {
                        for (Element region : subChild.getChildren()) {
                            regions.add(Region.getRegion(region));
                        }
                    }
                    int leak = 5;
                    if (subChild.getAttributeValue("leak") != null) {
                        leak = Integer.parseInt(subChild.getAttributeValue("leak").replaceAll(" ", ""));
                    } else if (child.getAttributeValue("leak") != null) {
                        leak = Integer.parseInt(child.getAttributeValue("leak").replaceAll(" ", ""));
                    } else if (element.getAttributeValue("leak") != null) {
                        leak = Integer.parseInt(element.getAttributeValue("leak").replaceAll(" ", ""));
                    }
                    Material type = Material.OBSIDIAN;
                    int damageValue = 0;
                    if (subChild.getAttributeValue("material") != null) {
                        String material = subChild.getAttributeValue("material");
                        if (material.contains(":")) {
                            type = Material.matchMaterial(material.split(":")[0].trim());
                            damageValue = Integer.parseInt(material.split(":")[1].trim());
                        } else {
                            type = Material.matchMaterial(material.trim());
                        }
                    } else if (child.getAttributeValue("material") != null) {
                        String material = child.getAttributeValue("material");
                        if (material.contains(":")) {
                            type = Material.matchMaterial(material.split(":")[0].trim());
                            damageValue = Integer.parseInt(material.split(":")[1].trim());
                        } else {
                            type = Material.matchMaterial(material.trim());
                        }
                    } else if (element.getAttributeValue("material") != null) {
                        String material = element.getAttributeValue("material");
                        if (material.contains(":")) {
                            type = Material.matchMaterial(material.split(":")[0].trim());
                            damageValue = Integer.parseInt(material.split(":")[1].trim());
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
                    result.add(new CoreObjective(team, name, id, new UnionRegion(regions), leak, type, damageValue, show));
                }
            }
        }
        return result;
    }

}
