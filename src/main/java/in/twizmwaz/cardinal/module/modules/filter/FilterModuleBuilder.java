package in.twizmwaz.cardinal.module.modules.filter;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.parsers.*;
import in.twizmwaz.cardinal.module.modules.filter.type.*;
import in.twizmwaz.cardinal.module.modules.filter.type.constant.*;
import org.jdom2.Document;
import org.jdom2.Element;

public class FilterModuleBuilder implements ModuleBuilder {
    
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for(Element element : match.getDocument().getRootElement().getChildren("filters")) {
            for (Element filter : element.getChildren("filter")) {
                
            }
        }
        return results;
    }

    /**
     * @param element  Element to parse
     * @param document Document to find filter in case the given filter is a reference
     * @return The filter based upon the given element
     */
    public static FilterModule getFilter(Element element, Document document) {
        switch (element.getName().toLowerCase()) {
            case "team":
                return new TeamFilter((new TeamFilterParser(element)));
            case "block":
                return new BlockFilter(new BlockFilterParser(element));
            case "spawn":
                return new SpawnFilter(new SpawnFilterParser(element));
            case "mob":
                return new MobFilter(new MobFilterParser(element));
            case "entity":
                return new EntityFilter(new EntityFilterParser(element));
            case "kill-streak":
                return new KillStreakFilter(new KillstreakFilterParser(element));
            case "class":
                return new ClassFilter(new FilterParser(element));
            case "random":
                return new RandomFilter(new RandomFilterParser(element));
            case "crouching":
                return new CrouchingFilter(new FilterParser(element));
            case "flying":
                return new FlyingFilter(new FilterParser(element));
            case "can-fly":
                return new FlyingAbilityFilter(new FilterParser(element));
            case "objective":
                return new ObjectiveFilter(new ObjectiveFilterParser(element));
            case "cause":
                return new CauseFilter(new CauseFilterParser(element));
            case "carrying":
                return new CarryingFilter(new ItemFilterParser(element));
            case "holding":
                return new HoldingFilter(new ItemFilterParser(element));
            case "wearing":
                return new WearingFilter(new ItemFilterParser(element));
            case "filter":
                switch (element.getAttributeValue("name").toLowerCase()) {
                    case "allow-all":
                        return new AllEventFilter("allow-all", true);
                    case "deny-all":
                        return new AllEventFilter("deny-all", false);
                    case "allow-players":
                        return new AllPlayerFilter("allow-players", true);
                    case "deny-players":
                        return new AllPlayerFilter("deny-players", false);
                    case "allow-blocks":
                        return new AllBlockFilter("allow-blocks", true);
                    case "deny-blocks":
                        return new AllBlockFilter("deny-blocks", false);
                    case "allow-world":
                        return new AllWorldFilter("allow-world", true);
                    case "deny-world":
                        return new AllWorldFilter("deny-world", false);
                    case "allow-spawns":
                        return new AllSpawnFilter("allow-spawns", true);
                    case "deny-spawns":
                        return new AllSpawnFilter("deny-spawns", false);
                    case "allow-entities":
                        return new AllEntitiesFilter("allow-entities", true);
                    case "deny-entities":
                        return new AllEntitiesFilter("deny-entities", false);
                    case "allow-mobs":
                        return new AllMobFilter("allow-mobs", true);
                    case "deny-mobs":
                        return new AllMobFilter("deny-mobs", false);
                    default:
                        if (element.getAttributeValue("name") != null) {
                            for (Element filterElement : document.getRootElement().getChildren("filters")) {
                                for (Element givenFilter : filterElement.getChildren()) {
                                    if (givenFilter.getAttributeValue("name").equalsIgnoreCase(element.getAttributeValue("name")))
                                        return getFilter(givenFilter);
                                }
                            }
                        } else {
                            return getFilter(element.getChildren().get(0));
                        }
                }
            default:
                return null;
        }
    }

    /**
     * This method will default the document to the document of the current match (possibly buggy)
     *
     * @param element Element to parse
     * @return The filter based upon the given element
     */
    public static FilterModule getFilter(Element element) {
        return getFilter(element, GameHandler.getGameHandler().getMatch().getDocument());
    }
}
