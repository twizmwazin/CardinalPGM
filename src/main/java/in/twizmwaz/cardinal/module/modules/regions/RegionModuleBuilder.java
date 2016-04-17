package in.twizmwaz.cardinal.module.modules.regions;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.regions.parsers.BlockParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.CircleParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.CuboidParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.CylinderParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.EmptyParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.EverywhereParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.PointParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.RectangleParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.SphereParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.TranslateParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.CircleRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.CuboidRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.CylinderRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.EmptyRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.EverywhereRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.RectangleRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.SphereRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.ComplementRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.IntersectRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.NegativeRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.modifications.MirroredRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.modifications.TranslatedRegion;
import in.twizmwaz.cardinal.util.Parser;
import org.jdom2.Document;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.EARLIEST)
public class RegionModuleBuilder implements ModuleBuilder {

    public static RegionModule getRegion(Element element, Document document) {
        RegionModule region;
        switch (element.getName().toLowerCase()) {
            case "block":
                region = new BlockRegion(new BlockParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "point":
                region = new PointRegion(new PointParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "circle":
                region = new CircleRegion(new CircleParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "cuboid":
                region = new CuboidRegion(new CuboidParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "cylinder":
                region = new CylinderRegion(new CylinderParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "empty":
                region = new EmptyRegion(new EmptyParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "nowhere":
                region = new EmptyRegion(new EmptyParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "everywhere":
                region = new EverywhereRegion(new EverywhereParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "rectangle":
                region = new RectangleRegion(new RectangleParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "sphere":
                region = new SphereRegion(new SphereParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "complement":
                region = new ComplementRegion(new CombinationParser(element, document));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "intersect":
                region = new IntersectRegion(new CombinationParser(element, document));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "negative":
                region = new NegativeRegion(new CombinationParser(element, document));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "union":
            case "regions":
                region = new UnionRegion( new CombinationParser(element, document));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "translate":
                region = new TranslatedRegion(new TranslateParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            case "mirror":
                region = new MirroredRegion(new MirrorParser(element));
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            default:
                if (element.getAttributeValue("name") != null) {
                    for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
                        if (element.getAttributeValue("name").equalsIgnoreCase(regionModule.getName())) {
                            return regionModule;
                        }
                    }
                } else if (element.getAttributeValue("id") != null) {
                    for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
                        if (element.getAttributeValue("id").equalsIgnoreCase(regionModule.getName())) {
                            return regionModule;
                        }
                    }
                } else if (element.getAttributeValue("region") != null) {
                    for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
                        if (element.getAttributeValue("region").equalsIgnoreCase(regionModule.getName())) {
                            return regionModule;
                        }
                    }
                } else {
                    return getRegion(element.getChildren().get(0));
                }
                return null;
        }
    }

    public static RegionModule getRegion(Element element) {
        return getRegion(element, GameHandler.getGameHandler().getMatch().getDocument());
    }

    public static RegionModule getRegion(String string) {
        for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
            if (string.equalsIgnoreCase(regionModule.getName())) return regionModule;
        }
        return null;
    }

    public static RegionModule getAttributeOrChild(String name, Element... elements) {
        String attr = Parser.getOrderedAttribute(name, elements);
        if (attr != null) return RegionModuleBuilder.getRegion(attr);
        else if (elements[0].getChild(name) != null) return RegionModuleBuilder.getRegion(elements[0].getChild(name));
        return null;
    }

    public static RegionModule getAttributeOrChild(String name, String fallback, Element... elements) {
        RegionModule region = getAttributeOrChild(name, elements);
        return region == null ? RegionModuleBuilder.getRegion(fallback) : region;
    }

    public static RegionModule getAttributeOrChild(String name, RegionModule fallback, Element... elements) {
        RegionModule region = getAttributeOrChild(name, elements);
        return region == null ? fallback : region;
    }

    @Override
    public ModuleCollection<RegionModule> load(Match match) {
        match.getModules().add(new EverywhereRegion("everywhere"));
        match.getModules().add(new EmptyRegion("nowhere"));
        ModuleCollection<RegionModule> results = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("regions")) {
            for (Element givenRegion : element.getChildren()) {
                for (Element givenChild : givenRegion.getChildren()) {
                    for (Element givenSubChild : givenChild.getChildren()) {
                        for (Element givenChildRegion : givenSubChild.getChildren()) {
                            getRegion(givenChildRegion);
                        }
                        getRegion(givenSubChild);
                    }
                    getRegion(givenChild);
                }
                if (!givenRegion.getName().equals("apply")) {
                    getRegion(givenRegion);
                }
            }
        }
        return results;
    }
}
