package in.twizmwaz.cardinal.module.modules.hill;

import com.google.common.collect.Sets;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.filter.parsers.BlockFilterParser;
import in.twizmwaz.cardinal.module.modules.filter.type.BlockFilter;
import in.twizmwaz.cardinal.module.modules.filter.type.logic.AnyFilter;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Material;
import org.jdom2.Element;

public class HillObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<HillObjective> load(Match match) {
        ModuleCollection<HillObjective> results = new ModuleCollection<>();
        for (Element king : match.getDocument().getRootElement().getChildren("king")) {
            for (Element hill : king.getChildren("hill")) {
                results.add(parseHill(true, hill, king));
            }
            for (Element hills : king.getChildren("hills")) {
                for (Element hill : hills.getChildren("hill")) {
                    results.add(parseHill(true, hill, hills, king));
                }
            }
        }
        for (Element king : match.getDocument().getRootElement().getChildren("control-points")) {
            for (Element hill : king.getChildren("control-point")) {
                results.add(parseHill(false, hill, king));
            }
            for (Element hills : king.getChildren("control-points")) {
                for (Element hill : hills.getChildren("control-point")) {
                    results.add(parseHill(false, hill, hills, king));
                }
            }
        }
        return results;
    }

    private HillObjective parseHill(boolean king, Element... elements) {
        String id = Parser.getOrderedAttribute("id", elements);
        String name = Parser.getOrderedAttribute("name", elements);
        double captureTime = 30;
        if (Parser.getOrderedAttribute("capture-time", elements) != null)
            captureTime = Strings.timeStringToExactSeconds(Parser.getOrderedAttribute("capture-time", elements));
        int points = Numbers.parseInt(Parser.getOrderedAttribute("points", elements), king ? 0 : 1);
        int pointsGrowth = Numbers.parseInt(Parser.getOrderedAttribute("points-growth", elements), 0);

        CaptureRule captureRule = CaptureRule.EXCLUSIVE;
        if (Parser.getOrderedAttribute("capture-rule", elements) != null)
            captureRule = CaptureRule.parseCaptureRule(Parser.getOrderedAttribute("capture-rule", elements));
        if (Parser.getOrderedAttribute("capture-players", elements) != null)
            captureRule = CaptureRule.parseCaptureRule(Parser.getOrderedAttribute("capture-players", elements));

        double timeMultiplier = Numbers.parseDouble(Parser.getOrderedAttribute("time-multiplier", elements), king ? 0.1 : 0);
        boolean showProgress = Numbers.parseBoolean(Parser.getOrderedAttribute("show-progress", elements), king);
        boolean neutralState = Numbers.parseBoolean(Parser.getOrderedAttribute("neutral-state", elements), king);
        boolean incremental = Numbers.parseBoolean(Parser.getOrderedAttribute("incremental", elements), king);
        boolean permanent = Numbers.parseBoolean(Parser.getOrderedAttribute("permanent", elements), king);

        TeamModule initialOwner = null;
        if (Parser.getOrderedAttribute("initial-owner", elements) != null)
            initialOwner = Teams.getTeamById(Parser.getOrderedAttribute("initial-owner", elements)).orNull();
        boolean show = Numbers.parseBoolean(Parser.getOrderedAttribute("show", elements), true);
        boolean required = Numbers.parseBoolean(Parser.getOrderedAttribute("required", elements), show);
        FilterModule visualMaterials = FilterModuleBuilder.getFilter(Parser.getOrderedAttribute("visual-materials", elements));
        if (visualMaterials == null) {
            ModuleCollection<FilterModule> blocks = new ModuleCollection<>();
            for (Material material : Sets.newHashSet(Material.WOOL, Material.CARPET, Material.STAINED_CLAY, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE)) {
                Element element = new Element("block");
                element.setText(material.toString());
                blocks.add(new BlockFilter(new BlockFilterParser(element)));
            }
            visualMaterials = new AnyFilter("visual-materials-" + name, blocks);
        }

        FilterModule captureFilter = FilterModuleBuilder.getAttributeOrChild("capture-filter", "always", elements);
        FilterModule playerFilter = FilterModuleBuilder.getAttributeOrChild("player-filter", "always", elements);

        RegionModule capture = RegionModuleBuilder.getAttributeOrChild("capture", elements);
        if (capture == null) capture = RegionModuleBuilder.getAttributeOrChild("capture-region", elements);

        RegionModule progress = RegionModuleBuilder.getAttributeOrChild("progress", elements);
        if (progress == null) progress = RegionModuleBuilder.getAttributeOrChild("progress-display-region", elements);

        RegionModule captured = RegionModuleBuilder.getAttributeOrChild("captured", elements);
        if (captured == null) captured = RegionModuleBuilder.getAttributeOrChild("owner-display-region", elements);

        return new HillObjective(initialOwner, name, id, captureTime, points, pointsGrowth, captureRule, timeMultiplier, showProgress, neutralState, incremental, permanent, show, required, capture, progress, captured, visualMaterials, captureFilter, playerFilter);
    }
}