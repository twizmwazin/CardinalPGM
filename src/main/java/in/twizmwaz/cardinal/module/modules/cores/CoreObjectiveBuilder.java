package in.twizmwaz.cardinal.module.modules.cores;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class CoreObjectiveBuilder implements ModuleBuilder {

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
                String name = "Core";
                if (subElement.getAttributeValue("name") != null) {
                    name = subElement.getAttributeValue("name");
                }
                if (element.getAttributeValue("name") != null) {
                    name = subElement.getAttributeValue("name");
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
                try {
                    leak = Integer.parseInt(subElement.getAttributeValue("leak").replaceAll(" ", ""));
                } catch (NullPointerException e) {
                }
                Material type;
                try {
                    type = Material.matchMaterial(element.getAttributeValue("material"));
                } catch (IllegalArgumentException e) {
                    type = Material.matchMaterial(subElement.getAttributeValue("material"));
                } catch (NullPointerException e) {
                    type = Material.matchMaterial(subElement.getAttributeValue("material"));
                }
                result.add(new CoreObjective(team, name, id, new UnionRegion(regions), leak, type));
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
                    String name = "Core";
                    try {
                        name = subChild.getAttributeValue("name");
                    } catch (NullPointerException e) {
                    }
                    if (element.getAttributeValue("name") != null) {
                        name = subChild.getAttributeValue("name");
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
                            regions.add(Region.getRegion(element));
                        }
                    }
                    int leak = 5;
                    try {
                        leak = Integer.parseInt(subChild.getAttributeValue("leak").replaceAll(" ", ""));
                    } catch (NullPointerException e) {
                    }
                    Material type;
                    try {
                        type = Material.matchMaterial(child.getAttributeValue("material"));
                    } catch (IllegalArgumentException e) {
                        type = Material.matchMaterial(subChild.getAttributeValue("material"));
                    } catch (NullPointerException e) {
                        type = Material.matchMaterial(subChild.getAttributeValue("material"));
                    }
                    result.add(new CoreObjective(team, name, id, new UnionRegion(regions), leak, type));
                }
            }
        }
        return result;
    }

}
