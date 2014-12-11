package in.twizmwaz.cardinal.teams.spawns;

import in.twizmwaz.cardinal.regions.Region;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 11/23/14.
 */
public class SpawnParser {

    public static List<Spawn> parseSpawns(Element element, String teamId) {
        List<Spawn> result = new ArrayList<Spawn>();

        if (element.getChildren("spawn").size() > 0) {
            for (Element child : element.getChildren("spawn")) {
                List<Region> reg = new ArrayList<Region>();
                int yaw = 0;
                String teamValue;
                yaw = Integer.parseInt(child.getAttributeValue("yaw"));
                teamValue = child.getAttributeValue("team");
                if (teamId.toLowerCase().startsWith(teamValue)) {
                    for (Element regionChild : child.getChildren()) {
                        reg.add(Region.newRegion(regionChild));
                    }
                    result.add(new Spawn(reg, yaw));
                }
            }
        }
        if (element.getChildren("spawns").size() > 0) {
            for (Element child : element.getChildren("spawns")) {
                for (Element subChild : child.getChildren("spawn")) {
                    String teamValue;
                    try {
                        teamValue = subChild.getAttributeValue("team");
                    } catch (NullPointerException ex) {
                        teamValue = child.getAttributeValue("team");
                    }
                    if (teamId.toLowerCase().startsWith(teamValue)) {
                        int yaw = Integer.parseInt(subChild.getAttributeValue("yaw"));
                        List<Region> reg = new ArrayList<Region>();
                        for (Element rege : subChild.getChildren()) {
                            reg.add(Region.newRegion(rege));
                        }
                        result.add(new Spawn(reg, yaw));
                    }
                }
            }
        }


        return result;
    }

    public static List<Spawn> parseDefault(Element element) {
        Element working = element.getChild("default");
        List<Spawn> result = new ArrayList<Spawn>();
        int yaw = Integer.parseInt(working.getAttributeValue("yaw"));
        List<Region> regions = new ArrayList<Region>();
        regions.add(Region.newRegion(working.getChildren().get(0)));
        result.add(new Spawn(regions, yaw));
        return result;
    }

}
