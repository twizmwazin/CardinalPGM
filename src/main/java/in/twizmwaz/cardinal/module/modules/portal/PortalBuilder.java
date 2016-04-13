package in.twizmwaz.cardinal.module.modules.portal;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jdom2.Element;

public class PortalBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Portal> load(Match match) {
        ModuleCollection<Portal> results = new ModuleCollection<>();
        for (Element portals : match.getDocument().getRootElement().getChildren("portals")) {
            for (Element portal : portals.getChildren("portal")) {
                results.add(parsePortal(portal, portals));
            }
            for (Element portalsChild : portals.getChildren("portals")) {
                for (Element portal : portalsChild.getChildren("portal")) {
                    results.add(parsePortal(portal, portalsChild, portals));
                }
            }
        }
        return results;
    }

    private Portal parsePortal(Element... elements) {
        Pair<Boolean, Double>
                x = getCoord("x", elements),
                y = getCoord("y", elements),
                z = getCoord("z", elements),
                yaw = getCoord("yaw", elements),
                pitch = getCoord("pitch", elements);

        RegionModule region = RegionModuleBuilder.getAttributeOrChild("region", elements);
        if (region == null) region = RegionModuleBuilder.getRegion(elements[0]);
        FilterModule filter = FilterModuleBuilder.getAttributeOrChild("filter", "always", elements);
        boolean sound = Numbers.parseBoolean(Parser.getOrderedAttribute("sound", elements), true);
        boolean protect = Numbers.parseBoolean(Parser.getOrderedAttribute("protect", elements), false);
        boolean bidirectional = Numbers.parseBoolean(Parser.getOrderedAttribute("bidirectional", elements), false);;
        RegionModule destination = RegionModuleBuilder.getAttributeOrChild("destination", elements);

        return new Portal(x, y, z, yaw, pitch, region, filter, sound, protect, bidirectional, destination);
    }

    private Pair<Boolean, Double> getCoord(String coord, Element... elements) {
        String attr = Parser.getOrderedAttribute(coord, elements);
        if (attr != null) {
            return new ImmutablePair<>(!attr.contains("@"), Numbers.parseDouble(attr.replaceAll("@", "")));
        }
        return new ImmutablePair<>(true, 0D);
    }

}
