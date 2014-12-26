package in.twizmwaz.cardinal.teams.spawns;

import in.twizmwaz.cardinal.regions.Region;
import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
                    int yaw = Integer.parseInt(spawnElement.getAttributeValue("yaw"));
                    String kit;
                    kit = spawnElement.getAttributeValue("kit");
                    if (kit == null) kit = subChild.getAttributeValue("kit");
                    if (kit == null) kit = child.getAttributeValue("kit");
                    List<Region> regions = new ArrayList<>();
                    for (Element regionElement : spawnElement.getChildren()) {
                        regions.add(Region.getRegion(regionElement));
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
                            yaw = Integer.parseInt(subChild.getChild("point").getAttributeValue("yaw"));
                        }
                    }

                }

                List<Region> regions = new ArrayList<Region>();
                for (Element regionElement : subChild.getChildren()) {
                    regions.add(Region.getRegion(regionElement));
                }
                if (teamId.toLowerCase().startsWith(teamValue)) {
                    result.add(new Spawn(regions, yaw, kit));
                }
            }
        }

        if (result.size() < 1) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to parse team Spawns!");
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
                regions.add(Region.getRegion(working.getChildren().get(0)));
                result.add(new Spawn(regions, yaw, null));
            } catch (NullPointerException e) {

            }

        }
        return result;
    }

}
