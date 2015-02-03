package in.twizmwaz.cardinal.module.modules.regions;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.regions.parsers.*;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.TranslateParser;
import in.twizmwaz.cardinal.module.modules.regions.type.*;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.ComplementRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.IntersectRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.NegativeRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.modifications.MirroredRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.modifications.TranslatedRegion;
import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.EARLIEST)
public class RegionModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
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
                CombinationParser parser = new CombinationParser(element, document);
                for (RegionModule regionChild : parser.getRegions()) {
                    GameHandler.getGameHandler().getMatch().getModules().add(regionChild);
                }
                region = new UnionRegion(parser);
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
            case "region":
                if (element.getAttributeValue("name") != null) {
                    for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
                        if (element.getAttributeValue("name").equalsIgnoreCase(regionModule.getName())) {
                            return regionModule;
                        }
                    }
                } else {
                    return getRegion(element.getChildren().get(0));
                }
            default:
                if (element.getAttributeValue("region") != null) {
                    for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
                        if (element.getAttributeValue("region").equalsIgnoreCase(regionModule.getName())) {
                            return regionModule;
                        }
                    }
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
}
