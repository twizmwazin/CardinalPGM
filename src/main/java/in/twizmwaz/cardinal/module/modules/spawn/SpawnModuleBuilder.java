package in.twizmwaz.cardinal.module.modules.spawn;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.*;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderData(load = ModuleLoadTime.EARLY)
public class SpawnModuleBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for (Element spawns : match.getDocument().getRootElement().getChildren("spawns")) {
            for (Element spawn : spawns.getChildren("spawn")) {
                TeamModule team = TeamUtils.getTeamById(spawns.getAttributeValue("team") != null ? spawns.getAttributeValue("team") : spawn.getAttributeValue("team"));
                List<Pair<Region, Vector>> regions = new ArrayList<>();
                for (Element region : spawn.getChildren()) {
                    Location location = new Location(GameHandler.getGameHandler().getMatchWorld(), 0, 0, 0);
                    if (region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw")));
                    if (region.getParentElement().getParentElement().getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getAttributeValue("yaw")));
                    if (region.getParentElement().getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getParentElement().getAttributeValue("yaw")));
                    if (region.getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getAttributeValue("yaw")));
                    regions.add(new ImmutablePair<>(Region.getRegion(region), location.getDirection()));
                }
                String kit = null;
                if (spawns.getAttributeValue("kit") != null)
                    kit = spawns.getAttributeValue("kit");
                if (spawn.getAttributeValue("kit") != null)
                    kit = spawn.getAttributeValue("kit");
                results.add(new SpawnModule(team, regions, kit, true, true));
            }
            for (Element spawn : spawns.getChildren("default")) {
                TeamModule team = TeamUtils.getTeamById("observers");
                List<Pair<Region, Vector>> regions = new ArrayList<>();
                for (Element region : spawn.getChildren()) {
                    Location location = new Location(GameHandler.getGameHandler().getMatchWorld(), 0, 0, 0);
                    if (region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw")));
                    if (region.getParentElement().getParentElement().getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getAttributeValue("yaw")));
                    if (region.getParentElement().getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getParentElement().getAttributeValue("yaw")));
                    if (region.getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getAttributeValue("yaw")));
                    regions.add(new ImmutablePair<>(Region.getRegion(region), location.getDirection()));
                }
                String kit = null;
                if (spawns.getAttributeValue("kit") != null)
                    kit = spawns.getAttributeValue("kit");
                if (spawn.getAttributeValue("kit") != null)
                    kit = spawn.getAttributeValue("kit");
                results.add(new SpawnModule(team, regions, kit, true, true));
            }
            for (Element element : spawns.getChildren("spawns")) {
                for (Element spawn : element.getChildren("spawn")) {
                    TeamModule team = TeamUtils.getTeamById(spawns.getAttribute("team").isSpecified() ? spawn.getAttributeValue("team") : element.getAttribute("team").isSpecified() ? element.getAttributeValue("team") : spawns.getAttributeValue("team"));
                    List<Pair<Region, Vector>> regions = new ArrayList<>();
                    for (Element region : spawn.getChildren()) {
                        Location location = new Location(GameHandler.getGameHandler().getMatchWorld(), 0, 0, 0);
                        if (region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw") != null)
                            location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw")));
                        if (region.getParentElement().getParentElement().getAttributeValue("yaw") != null)
                            location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getAttributeValue("yaw")));
                        if (region.getParentElement().getAttributeValue("yaw") != null)
                            location.setYaw(Float.parseFloat(region.getParentElement().getAttributeValue("yaw")));
                        if (region.getAttributeValue("yaw") != null)
                            location.setYaw(Float.parseFloat(region.getAttributeValue("yaw")));
                        regions.add(new ImmutablePair<>(Region.getRegion(region), location.getDirection()));
                    }
                    String kit = null;
                    if (spawns.getAttributeValue("kit") != null)
                        kit = spawns.getAttributeValue("kit");
                    if (element.getAttributeValue("kit") != null)
                        kit = element.getAttributeValue("kit");
                    if (spawn.getAttributeValue("kit") != null)
                        kit = spawn.getAttributeValue("kit");
                    results.add(new SpawnModule(team, regions, kit, true, true));
                }
            }

        }
        return results;
    }
}
