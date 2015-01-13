package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class DestroyableObjectiveBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> result = new ArrayList<>();
        for (Element element : match.getDocument().getRootElement().getChildren("cores")) {
            for (Element subElement : element.getChildren("core")) {
                PgmTeam team;
                try {
                    team = match.getTeamById(element.getAttributeValue("team"));
                } catch (NullPointerException e) {
                    team = match.getTeamById(subElement.getAttributeValue("team"));
                }
                String name = "Monument";
                if (subElement.getAttributeValue("name") != null) {
                    name = subElement.getAttributeValue("name");
                } else if (element.getAttributeValue("name") != null) {
                    name = element.getAttributeValue("name");
                }
                String id = null;
                try {
                    id = subElement.getAttributeValue("id");
                } catch (NullPointerException e) {
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
                if (element.getAttributeValue("material") != null) {
                    type = Material.matchMaterial(element.getAttributeValue("material"));
                } else if (subElement.getAttributeValue("material") != null) {
                    type = Material.matchMaterial(subElement.getAttributeValue("material"));
                }
                //result.add(new CoreObjective(team, name, id, new UnionRegion(regions), leak, type));
            }
            for (Element child : element.getChildren("cores")) {
                for (Element subChild : child.getChildren("core")) {
                    PgmTeam team;
                    try {
                        team = match.getTeamById(child.getAttributeValue("team"));
                    } catch (NullPointerException e) {
                        try {
                            team = match.getTeamById(subChild.getAttributeValue("team"));
                        } catch (NullPointerException ex) {
                            team = match.getTeamById(element.getAttributeValue("team"));
                        }
                    }
                    String name = "Monument";
                    if (subChild.getAttributeValue("name") != null) {
                        name = subChild.getAttributeValue("name");
                    } else if (child.getAttributeValue("name") != null) {
                        name = child.getAttributeValue("name");
                    } else if (element.getAttributeValue("name") != null) {
                        name = element.getAttributeValue("name");
                    }
                    String id = null;
                    try {
                        id = subChild.getAttributeValue("id");
                    } catch (NullPointerException e) {
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
                    if (subChild.getAttributeValue("material") != null) {
                        type = Material.matchMaterial(subChild.getAttributeValue("material"));
                    } else if (child.getAttributeValue("material") != null) {
                        type = Material.matchMaterial(child.getAttributeValue("material"));
                    } else if (element.getAttributeValue("material") != null) {
                        type = Material.matchMaterial(element.getAttributeValue("material"));
                    }
                    //result.add(new CoreObjective(team, name, id, new UnionRegion(regions), leak, type));
                }
            }
        }
        return result;
    }

}
