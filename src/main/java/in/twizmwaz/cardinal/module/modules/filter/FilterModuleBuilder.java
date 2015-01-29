package in.twizmwaz.cardinal.module.modules.filter;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.filter.parsers.*;
import in.twizmwaz.cardinal.module.modules.filter.type.*;
import in.twizmwaz.cardinal.module.modules.filter.type.constant.*;
import in.twizmwaz.cardinal.module.modules.filter.type.old.AllowFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.old.DenyFilter;
import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class FilterModuleBuilder implements ModuleBuilder {
    
    @Override
    public ModuleCollection load(Match match) {
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
        for(Element element : match.getDocument().getRootElement().getChildren("filters")) {
            for (Element filter : element.getChildren("filter")) {
                match.getModules().add(getFilter(filter.getChildren().get(0)));
            }
        }
        return new ModuleCollection<FilterModule>();
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
            case "allow":
                return new AllowFilter(new GenericFilterParser(element));
            case "deny":
                return new DenyFilter(new GenericFilterParser(element));
            case "void":
                return new VoidFilter(new GenericFilterParser(element));
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

    /**
     * Gets a loaded filter by the given name
     * @param string
     * @return
     */
    public static FilterModule getFilter(String string) {
        for (FilterModule filterModule : GameHandler.getGameHandler().getMatch().getModules().getModules(FilterModule.class)) {
            Bukkit.getLogger().info(string + " " + filterModule.getName());
            if (string.equalsIgnoreCase(filterModule.getName())) return filterModule;
        }
        return null;
    }
}
