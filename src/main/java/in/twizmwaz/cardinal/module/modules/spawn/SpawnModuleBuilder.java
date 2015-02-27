package in.twizmwaz.cardinal.module.modules.spawn;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.parsers.PointParser;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
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
    
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<SpawnModule> results = new ModuleCollection<SpawnModule>();
        for (Element spawns : match.getDocument().getRootElement().getChildren("spawns")) {
            for (Element spawn : spawns.getChildren("spawn")) {
                TeamModule team = TeamUtils.getTeamById(spawns.getAttributeValue("team") != null ? spawns.getAttributeValue("team") : spawn.getAttributeValue("team"));
                List<Pair<RegionModule, Vector>> regions = new ArrayList<>();
                for (Element region : spawn.getChildren()) {
                    RegionModule current = RegionModuleBuilder.getRegion(region);
                    Location location = new Location(GameHandler.getGameHandler().getMatchWorld(), 0, 0, 0);
                    if (region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw")));
                    if (region.getParentElement().getParentElement().getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getAttributeValue("yaw")));
                    if (region.getParentElement().getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getParentElement().getAttributeValue("yaw")));
                    if (region.getAttributeValue("yaw") != null)
                        location.setYaw(Float.parseFloat(region.getAttributeValue("yaw")));
                    if (region.getParentElement().getParentElement().getParentElement().getAttributeValue("angle") != null) 
                        location.setDirection(parseAngle(region.getParentElement().getParentElement().getParentElement().getAttributeValue("angle")).subtract(current.getCenterBlock().getVector()));
                    if (region.getParentElement().getParentElement().getAttributeValue("angle") != null)
                        location.setDirection(parseAngle(region.getParentElement().getParentElement().getAttributeValue("angle")).subtract(current.getCenterBlock().getVector()));
                    if (region.getParentElement().getAttributeValue("angle") != null) 
                        location.setDirection(parseAngle(region.getParentElement().getAttributeValue("angle")).subtract(current.getCenterBlock().getVector()));
                    regions.add(new ImmutablePair<>(current, location.getDirection()));
                }
                String kits = null;
                if (spawns.getAttributeValue("kit") != null)
                    kits = spawns.getAttributeValue("kit");
                if (spawn.getAttributeValue("kit") != null)
                    kits = spawn.getAttributeValue("kit");
                Kit kit = null;
                for (Kit kitModule : match.getModules().getModules(Kit.class)) {
                    if (kitModule.getName().equals(kits)) kit = kitModule;
                }
                results.add(new SpawnModule(team, regions, kit, true, true));
            }
            for (Element spawn : spawns.getChildren("default")) {
                TeamModule team = TeamUtils.getTeamById("observers");
                List<Pair<RegionModule, Vector>> regions = new ArrayList<>();
                if (spawn.getChildren().size() > 0) 
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
                    regions.add(new ImmutablePair<>(RegionModuleBuilder.getRegion(region), location.getDirection()));
                } else {
                    PointRegion point = new PointRegion(new PointParser(spawn));
                    regions.add(new ImmutablePair<RegionModule, Vector>(point, point.getLocation().getDirection()));
                }
                String kits = null;
                if (spawns.getAttributeValue("kit") != null)
                    kits = spawns.getAttributeValue("kit");
                if (spawn.getAttributeValue("kit") != null)
                    kits = spawn.getAttributeValue("kit");
                Kit kit = null;
                for (Kit kitModule : match.getModules().getModules(Kit.class)) {
                    if (kitModule.getName().equals(kits)) kit = kitModule;
                }
                results.add(new SpawnModule(team, regions, kit, true, true));
            }
            for (Element element : spawns.getChildren("spawns")) {
                for (Element spawn : element.getChildren("spawn")) {
                    TeamModule team = null;
                    if (spawns.getAttributeValue("team") != null) team = TeamUtils.getTeamById(spawns.getAttributeValue("team"));
                    if (element.getAttributeValue("team") != null) team = TeamUtils.getTeamById(element.getAttributeValue("team"));
                    if (spawn.getAttributeValue("team") != null) team = TeamUtils.getTeamById(spawn.getAttributeValue("team"));
                    List<Pair<RegionModule, Vector>> regions = new ArrayList<>();
                    for (Element region : spawn.getChildren()) {
                        RegionModule current = RegionModuleBuilder.getRegion(region);
                        Location location = new Location(GameHandler.getGameHandler().getMatchWorld(), 0, 0, 0);
                        if (region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw") != null)
                            location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw")));
                        if (region.getParentElement().getParentElement().getAttributeValue("yaw") != null)
                            location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getAttributeValue("yaw")));
                        if (region.getParentElement().getAttributeValue("yaw") != null)
                            location.setYaw(Float.parseFloat(region.getParentElement().getAttributeValue("yaw")));
                        if (region.getAttributeValue("yaw") != null)
                            location.setYaw(Float.parseFloat(region.getAttributeValue("yaw")));
                        if (region.getParentElement().getParentElement().getParentElement().getAttributeValue("angle") != null)
                            location.setDirection(parseAngle(region.getParentElement().getParentElement().getParentElement().getAttributeValue("angle")).subtract(current.getCenterBlock().getVector()));
                        if (region.getParentElement().getParentElement().getAttributeValue("angle") != null)
                            location.setDirection(parseAngle(region.getParentElement().getParentElement().getAttributeValue("angle")).subtract(current.getCenterBlock().getVector()));
                        if (region.getParentElement().getAttributeValue("angle") != null)
                            location.setDirection(parseAngle(region.getParentElement().getAttributeValue("angle")).subtract(current.getCenterBlock().getVector()));
                        regions.add(new ImmutablePair<>(current, location.getDirection()));
                    }
                    String kits = null;
                    if (spawns.getAttributeValue("kit") != null)
                        kits = spawns.getAttributeValue("kit");
                    if (element.getAttributeValue("kit") != null)
                        kits = element.getAttributeValue("kit");
                    if (spawn.getAttributeValue("kit") != null)
                        kits = spawn.getAttributeValue("kit");
                    Kit kit = null;
                    for (Kit kitModule : match.getModules().getModules(Kit.class)) {
                        if (kitModule.getName().equals(kits)) kit = kitModule;
                    }
                    results.add(new SpawnModule(team, regions, kit, true, true));
                }
            }
        }
        return results;
    }
    
    private Vector parseAngle(String string) {
        String[] loc = string.split(",");
        return new Vector(Double.parseDouble(loc[0].trim()), Double.parseDouble(loc[1].trim()),Double.parseDouble(loc[2].trim()));
    }
}
