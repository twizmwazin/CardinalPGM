package in.twizmwaz.cardinal.teams.spawns;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.BlockParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class SpawnParser {

    public static List<Spawn> parseSpawns(Document document, String teamId) {
        List<Spawn> result = new ArrayList<Spawn>();

        for (Element child : document.getRootElement().getChildren("spawns")) {

            for (Element subChild : child.getChildren("spawns")) {

                for (Element spawnElement : subChild.getChildren("spawn")) {
                    String teamValue;
                    teamValue = spawnElement.getAttributeValue("team");
                    if (teamValue == null) {
                        teamValue = subChild.getAttributeValue("team");
                    }
                    int yaw = 0;
                    try {
                        yaw = Integer.parseInt(child.getAttributeValue("yaw"));
                    } catch (NumberFormatException e) {
                    }
                    try {
                        yaw = Integer.parseInt(subChild.getAttributeValue("yaw"));
                    } catch (NumberFormatException e) {
                    }
                    try {
                        yaw = Integer.parseInt(spawnElement.getAttributeValue("yaw"));
                    } catch (NumberFormatException e) {
                    }
                    String kit;
                    kit = spawnElement.getAttributeValue("kit");
                    if (kit == null) kit = subChild.getAttributeValue("kit");
                    if (kit == null) kit = child.getAttributeValue("kit");
                    List<Region> regions = new ArrayList<>();
                    for (Element regionElement : spawnElement.getChildren()) {
                        regions.add(Region.getRegion(regionElement, document));
                    }
                    if (teamId.toLowerCase().startsWith(teamValue)) {
                        result.add(new Spawn(regions, yaw, kit));
                    }
                }
            }

            for (Element subChild : child.getChildren("spawn")) {
                String teamValue;
                teamValue = child.getAttributeValue("team");
                if (teamValue == null) {
                    teamValue = subChild.getAttributeValue("team");
                }
                String kit;
                kit = subChild.getAttributeValue("kit");
                if (kit == null) {
                    kit = child.getAttributeValue("kit");
                }
                int yaw;
                try {
                    String[] angle;
                    try {
                        angle = subChild.getAttributeValue("angle").split(",");
                    } catch (NullPointerException e) {
                        angle = child.getAttributeValue("angle").split(",");
                    }
                    yaw = 0;
                } catch (NullPointerException exc) {
                    try {
                        yaw = Integer.parseInt(subChild.getAttributeValue("yaw"));
                    } catch (NumberFormatException e) {
                        try {
                            yaw = Integer.parseInt(child.getAttributeValue("yaw"));
                        } catch (NumberFormatException ex) {
                            yaw = Integer.parseInt(subChild.getChildren().get(0).getAttributeValue("yaw"));
                        }
                    }
                }
                List<Region> regions = new ArrayList<Region>();
                for (Element regionElement : subChild.getChildren()) {
                    regions.add(Region.getRegion(regionElement, document));
                }
                if (teamId.toLowerCase().startsWith(teamValue)) {
                    result.add(new Spawn(regions, yaw, kit));
                }
            }
        }
        return result;
    }

    public static List<Spawn> parseDefault(Document document) {
        List<Spawn> result = new ArrayList<Spawn>();
        for (Element spawns : document.getRootElement().getChildren("spawns")) {
            try {
                Element working = spawns.getChild("default");
                int yaw = 0;
                try {
                    yaw = Integer.parseInt(spawns.getAttributeValue("yaw"));
                } catch (Exception e) {}
                try {
                    yaw = Integer.parseInt(working.getAttributeValue("yaw"));
                } catch (Exception e) {}
                try {
                    yaw = Integer.parseInt(working.getChild("point").getAttributeValue("yaw"));
                } catch (Exception e) {}
                List<Region> regions = new ArrayList<Region>();
                try {
                    regions.add(Region.getRegion(working.getChildren().get(0), document));
                } catch (IndexOutOfBoundsException e) {
                    regions.add(new BlockRegion(new BlockParser(working)));
                }
                String kit = working.getAttributeValue("kit");
                result.add(new Spawn(regions, yaw, kit));
            } catch (NullPointerException e) {

            }

        }
        return result;
    }

}
