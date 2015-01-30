package in.twizmwaz.cardinal.module.modules.portal;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class PortalBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<Portal> results = new ModuleCollection<>();
        for (Element portals : match.getDocument().getRootElement().getChildren("portals")) {
            for (Element portal : portals.getChildren("portal")) {
                double x = 0;
                boolean xRelative = true;
                if (portal.getAttributeValue("x") != null) {
                    x = NumUtils.parseDouble(portal.getAttributeValue("x").replaceAll("@", ""));
                    xRelative = !portal.getAttributeValue("x").contains("@");
                } else if (portals.getAttributeValue("x") != null) {
                    x = NumUtils.parseDouble(portals.getAttributeValue("x"));
                    xRelative = !portals.getAttributeValue("x").contains("@");
                }
                double y = 0;
                boolean yRelative = true;
                if (portal.getAttributeValue("y") != null) {
                    y = NumUtils.parseDouble(portal.getAttributeValue("y").replaceAll("@", ""));
                    yRelative = !portal.getAttributeValue("y").contains("@");
                } else if (portals.getAttributeValue("y") != null) {
                    y = NumUtils.parseDouble(portals.getAttributeValue("y"));
                    yRelative = !portals.getAttributeValue("y").contains("@");
                }
                double z = 0;
                boolean zRelative = true;
                if (portal.getAttributeValue("z") != null) {
                    z = NumUtils.parseDouble(portal.getAttributeValue("z").replaceAll("@", ""));
                    zRelative = !portal.getAttributeValue("z").contains("@");
                } else if (portals.getAttributeValue("z") != null) {
                    z = NumUtils.parseDouble(portals.getAttributeValue("z"));
                    zRelative = !portals.getAttributeValue("z").contains("@");
                }
                RegionModule region = null;
                if (portal.getAttributeValue("region") != null) {
                    region = RegionModuleBuilder.getRegion(portal);
                } else if (portals.getAttributeValue("region") != null) {
                    region = RegionModuleBuilder.getRegion(portals);
                } else {
                    for (Element child : portal.getChildren()) {
                        if (!child.getName().equalsIgnoreCase("destination")) {
                            region = RegionModuleBuilder.getRegion(child);
                        }
                    }
                    if (region == null) {
                        region = RegionModuleBuilder.getRegion(portal.getChild("region").getChildren().get(0));
                    }
                }
                FilterModule filter = null;
                if (portal.getAttributeValue("filter") != null) {
                    filter = FilterModuleBuilder.getFilter(portal.getAttributeValue("filter"));
                } else if (portals.getAttributeValue("filter") != null) {
                    filter = FilterModuleBuilder.getFilter(portals.getAttributeValue("filter"));
                }
                boolean sound = true;
                if (portal.getAttributeValue("sound") != null) {
                    sound = !portal.getAttributeValue("sound").equalsIgnoreCase("false");
                } else if (portals.getAttributeValue("sound") != null) {
                    sound = !portals.getAttributeValue("sound").equalsIgnoreCase("false");
                }
                boolean protect = false;
                if (portal.getAttributeValue("protect") != null) {
                    protect = portal.getAttributeValue("protect").equalsIgnoreCase("true");
                } else if (portals.getAttributeValue("protect") != null) {
                    protect = portals.getAttributeValue("protect").equalsIgnoreCase("true");
                }
                boolean bidirectional = false;
                if (portal.getAttributeValue("bidirectional") != null) {
                    bidirectional = portal.getAttributeValue("bidirectional").equalsIgnoreCase("true");
                } else if (portals.getAttributeValue("bidirectional") != null) {
                    bidirectional = portals.getAttributeValue("bidirectional").equalsIgnoreCase("true");
                }
                int yaw = 0;
                boolean yawRelative = true;
                if (portal.getAttributeValue("yaw") != null) {
                    yaw = (int) NumUtils.parseDouble(portal.getAttributeValue("yaw").replaceAll("@", ""));
                    yawRelative = !portal.getAttributeValue("yaw").contains("@");
                } else if (portals.getAttributeValue("yaw") != null) {
                    yaw = (int) NumUtils.parseDouble(portals.getAttributeValue("yaw"));
                    yawRelative = !portals.getAttributeValue("yaw").contains("@");
                }
                int pitch = 0;
                boolean pitchRelative = true;
                if (portal.getAttributeValue("pitch") != null) {
                    pitch = (int) NumUtils.parseDouble(portal.getAttributeValue("pitch").replaceAll("@", ""));
                    pitchRelative = !portal.getAttributeValue("pitch").contains("@");
                } else if (portals.getAttributeValue("pitch") != null) {
                    pitch = (int) NumUtils.parseDouble(portals.getAttributeValue("pitch"));
                    pitchRelative = !portals.getAttributeValue("pitch").contains("@");
                }
                RegionModule destination = null;
                if (portal.getChild("destination") != null) {
                    destination = RegionModuleBuilder.getRegion(portal.getChild("destination").getChildren().get(0));
                }
                results.add(new Portal(new Vector(x, y, z), xRelative, yRelative, zRelative, region, filter, sound, protect, bidirectional, yaw, yawRelative, pitch, pitchRelative, destination));
            }
        }
        return results;
    }

}
