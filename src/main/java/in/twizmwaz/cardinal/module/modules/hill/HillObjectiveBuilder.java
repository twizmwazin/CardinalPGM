package in.twizmwaz.cardinal.module.modules.hill;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.StringUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.jdom2.Element;

public class HillObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<HillObjective> results = new ModuleCollection<HillObjective>();
        for (Element king : match.getDocument().getRootElement().getChildren("king")) {
            for (Element hill : king.getChildren("hill")) {
                results.add(parseHill(hill));
            }
            for (Element hills : king.getChildren("hills")) {
                for (Element hill : hills.getChildren("hill")) {
                    results.add(parseHill(hill));
                }
            }
        }
        for (Element king : match.getDocument().getRootElement().getChildren("control-points")) {
            for (Element hill : king.getChildren("control-point")) {
                results.add(parseHill(hill));
            }
            for (Element hills : king.getChildren("control-points")) {
                for (Element hill : hills.getChildren("control-point")) {
                    results.add(parseHill(hill));
                }
            }
        }
        return results;
    }
    
    private HillObjective parseHill(Element element) {
        String id = element.getAttributeValue("id");
        String name = element.getAttributeValue("name");
        int capturetime = 30;
        if (element.getParentElement().getAttributeValue("capture-time") != null) 
            capturetime = StringUtils.timeStringToSeconds(element.getParentElement().getAttributeValue("capture-time"));
        if (element.getAttributeValue("capture-time") != null) 
            capturetime = StringUtils.timeStringToSeconds(element.getAttributeValue("capture-time"));
        int points = 1;
        if (element.getParentElement().getAttributeValue("points") != null)
            points = Integer.parseInt(element.getParentElement().getAttributeValue("points"));
        if (element.getAttributeValue("points") != null)
            points = Integer.parseInt(element.getAttributeValue("points"));
        double pointsGrowth = 0;
        if (element.getParentElement().getAttributeValue("points-growth") != null)
            pointsGrowth = Double.parseDouble(element.getParentElement().getAttributeValue("points-growth"));
        if (element.getAttributeValue("points-growth") != null)
            pointsGrowth = Double.parseDouble(element.getAttributeValue("points-growth"));
        CaptureRule captureRule = CaptureRule.EXCLUSIVE;
        if (element.getAttributeValue("capture-rule") != null) CaptureRule.parseCaptureRule(element.getAttributeValue("capture-rule"));
        if (element.getParentElement().getAttributeValue("capture-rule") != null) CaptureRule.parseCaptureRule(element.getParentElement().getAttributeValue("capture-rule"));
        double timeMultiplier = 0;
        if (element.getParentElement().getAttributeValue("time-multiplier") != null)
            timeMultiplier = Double.parseDouble(element.getParentElement().getAttributeValue("time-multiplier"));
        if (element.getAttributeValue("time-multiplier") != null)
            timeMultiplier = Double.parseDouble(element.getAttributeValue("time-multiplier"));
        boolean showProgress = false;
        if (element.getParentElement().getAttributeValue("show-progress") != null)
            showProgress = NumUtils.parseBoolean(element.getParentElement().getAttributeValue("show-progress"));
        if (element.getAttributeValue("show-progress") != null)
            showProgress = NumUtils.parseBoolean(element.getAttributeValue("show-progress"));
        boolean neutralState = false;
        if (element.getParentElement().getAttributeValue("neutral-state") != null)
            neutralState = NumUtils.parseBoolean(element.getParentElement().getAttributeValue("neutral-state"));
        if (element.getAttributeValue("neutral-state") != null)
            neutralState = NumUtils.parseBoolean(element.getAttributeValue("neutral-state"));
        boolean incremental = false;
        if (element.getParentElement().getAttributeValue("incremental") != null)
            incremental = NumUtils.parseBoolean(element.getParentElement().getAttributeValue("incremental"));
        if (element.getAttributeValue("incremental") != null)
            incremental = NumUtils.parseBoolean(element.getAttributeValue("incremental"));
        boolean permanent = false;
        if (element.getParentElement().getAttributeValue("permanent") != null)
            permanent = NumUtils.parseBoolean(element.getParentElement().getAttributeValue("permanent"));
        if (element.getAttributeValue("permanent") != null)
            permanent = NumUtils.parseBoolean(element.getAttributeValue("permanent"));
        TeamModule initialOwner = null;
        if (element.getParentElement().getAttributeValue("initial-owner") != null)
            initialOwner = TeamUtils.getTeamById(element.getParentElement().getAttributeValue("initial-owner"));
        if (element.getAttributeValue("initial-owner") != null)
            initialOwner = TeamUtils.getTeamById(element.getAttributeValue("initial-owner"));
        boolean show = true;
        if (element.getParentElement().getAttributeValue("show") != null)
            show = NumUtils.parseBoolean(element.getParentElement().getAttributeValue("show"));
        if (element.getAttributeValue("showl") != null)
            show = NumUtils.parseBoolean(element.getAttributeValue("show"));
        String materials = element.getAttributeValue("visual-materials") == null ? 
                element.getParentElement().getAttributeValue("visual-materials") : 
                element.getAttributeValue("visual-materials");
        FilterModule visualMaterials = FilterModuleBuilder.getFilter(materials);
        RegionModule capture = element.getAttributeValue("capture") == null ?
                RegionModuleBuilder.getRegion(element.getChild("capture")) :
                RegionModuleBuilder.getRegion(element.getAttributeValue("capture"));
        RegionModule progress = element.getAttributeValue("progress") == null ?
                RegionModuleBuilder.getRegion(element.getChild("progress")) :
                RegionModuleBuilder.getRegion(element.getAttributeValue("progress"));
        RegionModule captured = element.getAttributeValue("captured") == null ?
                RegionModuleBuilder.getRegion(element.getChild("captured")) :
                RegionModuleBuilder.getRegion(element.getAttributeValue("captured"));
        return new HillObjective(initialOwner, name, id, capturetime, points, pointsGrowth, captureRule, timeMultiplier, showProgress, neutralState, incremental, permanent, show, capture, progress, captured);
    }
}