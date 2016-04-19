package in.twizmwaz.cardinal.module.modules.ctf.post;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.parsers.PointParser;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import org.jdom2.Element;

import java.util.List;

public class PostBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Post> results = new ModuleCollection<>();
        for (Element flags : match.getDocument().getRootElement().getChildren("flags")) {
            for (Element post : flags.getChildren("post")) {
                results.add(parsePostElement(post));
            }
            for (Element flags2 : flags.getChildren("flags")) {
                for (Element post : flags2.getChildren("post")) {
                    results.add(parsePostElement(post));
                }
            }
        }
        return results;
    }

    public static Post parsePostElement(Element elements) {
        if (elements.getName().toLowerCase().equals("post")) {
            List<RegionModule> regions = Lists.newArrayList();
            if (elements.getChildren().size() > 0) {
                for (Element e : elements.getChildren()) {
                    regions.add(RegionModuleBuilder.getRegion(e));
                }
            } else {
                regions.add(new PointRegion(new PointParser(elements)));
            }
            String id = elements.getAttributeValue("id") == null ? null : elements.getAttributeValue("id");
            TeamModule owner = elements.getAttributeValue("owner") == null ? null : Teams.getTeamById(elements.getAttributeValue("owner")).orNull();
            boolean permanent = Boolean.parseBoolean(elements.getAttributeValue("permanent", "false"));
            boolean sequential = Boolean.parseBoolean(elements.getAttributeValue("sequential", "false"));
            int pointsRate = Numbers.parseInt(elements.getAttributeValue("points-rate", "0"));
            FilterModule pickupFilter = FilterModuleBuilder.getAttributeOrChild("pickup-filter", elements);
            int recoverTime = 30;
            if (elements.getAttributeValue("recover-time") != null) {
                recoverTime = Strings.timeStringToSeconds(elements.getAttributeValue("recover-time"));
            } else if (elements.getAttributeValue("return-time") != null) {
                recoverTime = Strings.timeStringToSeconds(elements.getAttributeValue("return-time"));
            }
            int respawnTime = Strings.timeStringToSeconds(elements.getAttributeValue("respawn-time", "-1s"));
            int respawnSpeed = Numbers.parseInt(elements.getAttributeValue("respawn-speed", "8"));
            float yaw = Float.MIN_VALUE;
            if (elements.getAttributeValue("yaw") != null) {
                yaw = Float.parseFloat(elements.getAttributeValue("yaw"));
                yaw = Math.min(180, Math.max(-180, yaw));
            }
            return new Post(regions, id, owner, permanent, sequential, pointsRate, pickupFilter, recoverTime, respawnTime, respawnSpeed, yaw);
        }
        return null;
    }
}
