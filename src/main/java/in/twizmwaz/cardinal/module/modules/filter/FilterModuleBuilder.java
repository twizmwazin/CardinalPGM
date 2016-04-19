package in.twizmwaz.cardinal.module.modules.filter;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.filter.parsers.BlockFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.CauseFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ChildFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ChildrenFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ClassFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.EntityFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.FlagFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.FlagFilterPostParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.GenericFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ItemFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.KillstreakFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.MobFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ObjectiveFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.RandomFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.SpawnFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.TeamFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.parsers.TimeFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.type.BlockFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.CarryingFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.CauseFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.ClassFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.CrouchingFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.EntityFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.FlyingAbilityFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.FlyingFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.HoldingFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.KillStreakFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.MobFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.ObjectiveFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.RandomFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.SameTeamFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.SpawnFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.TeamFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.TimeFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.VoidFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.WearingFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.constant.AllBlockFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.constant.AllEntitiesFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.constant.AllEventFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.constant.AllMobFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.constant.AllPlayerFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.constant.AllSpawnFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.constant.AllWorldFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.flag.FlagCapturedFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.flag.FlagCarriedFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.flag.FlagCarryingFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.flag.FlagDroppedFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.flag.FlagReturnedFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.logic.AllFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.logic.AnyFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.logic.NotFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.logic.OneFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.old.AllowFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.old.DenyFilter;
import in.twizmwaz.cardinal.util.Parser;
import org.jdom2.Document;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.EARLY)
public class FilterModuleBuilder implements ModuleBuilder {

    /**
     * @param element  Element to parse
     * @param document Document to find filter in case the given filter is a reference
     * @return The filter based upon the given element
     */
    public static FilterModule getFilter(Element element, Document document) {
        String id = element.getAttributeValue("name") != null ? element.getAttributeValue("name") :
                (element.getAttributeValue("id") != null ? element.getAttributeValue("id") : null);
        switch (element.getName().toLowerCase()) {
            case "block":
                return new BlockFilter(new BlockFilterParser(element));
            case "carrying":
                return new CarryingFilter(new ItemFilterParser(element));
            case "cause":
                return new CauseFilter(new CauseFilterParser(element));
            case "class":
                return new ClassFilter(new ClassFilterParser(element));
            case "crouching":
                return new CrouchingFilter(new FilterParser(element));
            case "entity":
                return new EntityFilter(new EntityFilterParser(element));
            case "can-fly":
                return new FlyingAbilityFilter(new FilterParser(element));
            case "flying":
                return new FlyingFilter(new FilterParser(element));
            case "holding":
                return new HoldingFilter(new ItemFilterParser(element));
            case "kill-streak":
                return new KillStreakFilter(new KillstreakFilterParser(element));
            case "material":
                return new BlockFilter(new BlockFilterParser(element));
            case "mob":
                return new MobFilter(new MobFilterParser(element));
            case "objective":
                return new ObjectiveFilter(new ObjectiveFilterParser(element));
            case "random":
                return new RandomFilter(new RandomFilterParser(element));
            case "spawn":
                return new SpawnFilter(new SpawnFilterParser(element));
            case "team":
                return new TeamFilter((new TeamFilterParser(element)));
            case "time":
                return new TimeFilter(new TimeFilterParser(element));
            case "void":
                return new VoidFilter(new GenericFilterParser(element));
            case "wearing":
                return new WearingFilter(new ItemFilterParser(element));
            case "flag-carried":
                return new FlagCarriedFilter(new FlagFilterPostParser(element));
            case "flag-dropped":
                return new FlagDroppedFilter(new FlagFilterPostParser(element));
            case "flag-returned":
                return new FlagReturnedFilter(new FlagFilterPostParser(element));
            case "flag-captured":
                return new FlagCapturedFilter(new FlagFilterPostParser(element));
            case "carrying-flag":
                return new FlagCarryingFilter(new FlagFilterParser(element));
            case "same-team":
                return new SameTeamFilter(new ChildFilterParser(element));
            case "all":
                return new AllFilter(new ChildrenFilterParser(element));
            case "any":
                return new AnyFilter(new ChildrenFilterParser(element));
            case "not":
                return new NotFilter(new ChildrenFilterParser(element));
            case "one":
                return new OneFilter(new ChildrenFilterParser(element));
            case "allow":
                return new AllowFilter(new ChildrenFilterParser(element));
            case "deny":
                return new DenyFilter(new ChildrenFilterParser(element));
            default:
                if (element.getChildren().size() > 0) {
                    if (element.getChildren().size() > 1) {
                        return new AllFilter(new ChildrenFilterParser(element));
                    } else {
                        return getFilter(element.getChildren().get(0));
                    }
                } else if (id != null) {
                    switch (id.toLowerCase()) {
                        case "always":
                            return new AllEventFilter("always", true);
                        case "never":
                            return new AllEventFilter("never", false);
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
                        case "allow":
                            return new AllowFilter(new ChildrenFilterParser(element));
                        case "deny":
                            return new DenyFilter(new ChildrenFilterParser(element));
                        default:
                            for (Element filterElement : document.getRootElement().getChildren("filters")) {
                                for (Element givenFilter : filterElement.getChildren()) {
                                    if (givenFilter.getAttributeValue("name") != null && givenFilter.getAttributeValue("name").equalsIgnoreCase(id))
                                        return getFilter(givenFilter.getChildren().get(0));
                                    if (givenFilter.getAttributeValue("id") != null && givenFilter.getAttributeValue("id").equalsIgnoreCase(id))
                                        return getFilter(givenFilter.getChildren().get(0));
                                }
                            }
                    }
                } else {
                    return getFilter(element.getChildren().get(0));
                }
        }
        return null;
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

    /**
     * Gets a loaded filter by the given name
     *
     * @param string
     * @return
     */
    public static FilterModule getFilter(String string) {
        if (string == null) return null;
        for (FilterModule filterModule : GameHandler.getGameHandler().getMatch().getModules().getModules(FilterModule.class)) {
            if (string.equalsIgnoreCase(filterModule.getName())) return filterModule;
        }
        return null;
    }

    public static FilterModule getAttributeOrChild(String name, Element... elements) {
        String attr = Parser.getOrderedAttribute(name, elements);
        if (attr != null) return getFilter(attr);
        else if (elements[0].getChild(name) != null) return getFilter(elements[0].getChild(name));
        return null;
    }

    public static FilterModule getAttributeOrChild(String name, String fallback, Element... elements) {
        FilterModule region = getAttributeOrChild(name, elements);
        return region == null ? getFilter(fallback) : region;
    }

    public static FilterModule getAttributeOrChild(String name, FilterModule fallback, Element... elements) {
        FilterModule region = getAttributeOrChild(name, elements);
        return region == null ? fallback : region;
    }

    @Override
    public ModuleCollection<FilterModule> load(Match match) {
        match.getModules().add(new AllEventFilter("always", true));
        match.getModules().add(new AllEventFilter("never", false));
        match.getModules().add(new AllEventFilter("allow-all", true));
        match.getModules().add(new AllEventFilter("deny-all", false));
        match.getModules().add(new AllPlayerFilter("allow-players", true));
        match.getModules().add(new AllPlayerFilter("deny-players", false));
        match.getModules().add(new AllBlockFilter("allow-blocks", true));
        match.getModules().add(new AllBlockFilter("deny-blocks", false));
        match.getModules().add(new AllWorldFilter("allow-world", true));
        match.getModules().add(new AllWorldFilter("deny-world", false));
        match.getModules().add(new AllSpawnFilter("allow-spawns", true));
        match.getModules().add(new AllSpawnFilter("deny-spawns", false));
        match.getModules().add(new AllEntitiesFilter("allow-entities", true));
        match.getModules().add(new AllEntitiesFilter("deny-entities", false));
        match.getModules().add(new AllMobFilter("allow-mobs", true));
        match.getModules().add(new AllMobFilter("deny-mobs", false));
        for (Element element : match.getDocument().getRootElement().getChildren("filters")) {
            for (Element filter : element.getChildren()) {
                match.getModules().add(getFilter(filter));
            }
        }
        return new ModuleCollection<>();
    }
}
