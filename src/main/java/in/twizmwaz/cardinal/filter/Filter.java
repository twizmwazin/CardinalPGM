package in.twizmwaz.cardinal.filter;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.filter.parsers.*;
import in.twizmwaz.cardinal.filter.type.*;
import in.twizmwaz.cardinal.filter.type.constant.*;
import org.bukkit.event.Event;
import org.jdom2.Document;
import org.jdom2.Element;

public abstract class Filter {

    /**
     * @param event The event which will be filters
     * @return The state of the filter.
     */
    public abstract FilterState evaluate(final Event event);

    /**
     * @param element Element to parse
     * @param document Document to find filter in case the given filter is a reference
     * @return The filter based upon the given element
     */
    public static Filter getFilter(Element element, Document document) {
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
                return new ClassFilter();
            case "random":
                return new RandomFilter(new RandomFilterParser(element));
            case "crouching":
                return new CrouchingFilter();
            case "flying":
                return new FlyingFilter();
            case "can-fly":
                return new FlyingAbilityFilter();
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
                        return new AllEventFilter(true);
                    case "deny-all":
                        return new AllEventFilter(false);
                    case "allow-players":
                        return new AllPlayerFilter(true);
                    case "deny-players":
                        return new AllPlayerFilter(false);
                    case "allow-blocks":
                        return new AllBlockFilter(true);
                    case "deny-blocks":
                        return new AllBlockFilter(false);
                    case "allow-world":
                        return new AllWorldFilter(true);
                    case "deny-world":
                        return new AllWorldFilter(false);
                    case "allow-spawns":
                        return new AllSpawnFilter(true);
                    case "deny-spawns":
                        return new AllSpawnFilter(false);
                    case "allow-entities":
                        return new AllEntitiesFilter(true);
                    case "deny-entities":
                        return new AllEntitiesFilter(false);
                    case "allow-mobs":
                        return new AllMobFilter(true);
                    case "deny-mobs":
                        return new AllMobFilter(false);
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
     * @param element Element to parse
     * @return The filter based upon the given element
     */
    public static Filter getFilter(Element element) {
        return getFilter(element, GameHandler.getGameHandler().getMatch().getDocument());
    }

}
