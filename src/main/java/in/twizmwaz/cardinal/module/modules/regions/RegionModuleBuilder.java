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
                    for (Element givenSubChild : givenRegion.getChildren()) {
                        RegionModule staged = getRegion(givenSubChild);
                        if (staged.getName() != null) match.getModules().add(staged);
                    }
                    RegionModule staged = getRegion(givenChild);
                    if (staged.getName() != null) match.getModules().add(staged);
                }
                if (!givenRegion.getName().equals("apply")) {
                    RegionModule staged = getRegion(givenRegion);
                    if (staged.getName() != null) match.getModules().add(staged);
                }
            }
        }
        return results;
    }

    public static RegionModule getRegion(Element element, Document document) {
        switch (element.getName().toLowerCase()) {
            case "block":
                return new BlockRegion(new BlockParser(element));
            case "point":
                return new PointRegion(new PointParser(element));
            case "circle":
                return new CircleRegion(new CircleParser(element));
            case "cuboid":
                return new CuboidRegion(new CuboidParser(element));
            case "cylinder":
                return new CylinderRegion(new CylinderParser(element));
            case "empty":
                return new EmptyRegion(new EmptyParser(element));
            case "rectangle":
                return new RectangleRegion(new RectangleParser(element));
            case "sphere":
                return new SphereRegion(new SphereParser(element));
            case "complement":
                return new ComplementRegion(new CombinationParser(element, document));
            case "intersect":
                return new IntersectRegion(new CombinationParser(element, document));
            case "negative":
                return new NegativeRegion(new CombinationParser(element, document));
            case "union":
            case "regions":
                return new UnionRegion((new CombinationParser(element, document)));
            case "translate":
                return new TranslatedRegion(new TranslateParser(element));
            case "mirror":
                return new MirroredRegion(new MirrorParser(element));
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
                    for (Element regionElement : document.getRootElement().getChildren("regions")) {
                        for (Element givenRegion : regionElement.getChildren()) {
                            for (Element givenChild : givenRegion.getChildren()) {
                                if (givenChild.getAttributeValue("region").equalsIgnoreCase(element.getAttributeValue("region"))) {
                                    return getRegion(givenChild);
                                }
                            }
                            if (givenRegion.getName().equalsIgnoreCase("apply")) continue;
                            if (givenRegion.getAttributeValue("region").equalsIgnoreCase(element.getAttributeValue("region"))) {
                                return getRegion(givenRegion);
                            }
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

