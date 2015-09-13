package in.twizmwaz.cardinal.module.modules.ctf.post;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.GameHandler;
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
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.jdom2.Element;

import java.util.List;

public class PostBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Post> results = new ModuleCollection<>();
        for (Element flags : match.getDocument().getRootElement().getChildren("flags")) {
            for (Element post : flags.getChildren("post")) {
                Post postp = parsePostElement(post);
                Log.info("Added postp " + postp.getId());
                results.add(postp);
            }
            for (Element subFlags : flags.getChildren("flags")) {
                for (Element post : subFlags.getChildren("post")) {
                    Post postp = parsePostElement(post);
                    Log.info("Added postp " + postp.getId());
                    results.add(postp);
                }
            }
        }
        return results;
    }

    public static Post getPost(String id) {
        for (Post post : GameHandler.getGameHandler().getMatch().getModules().getModules(Post.class)) {
            if (post.getId().toLowerCase().equals(id.toLowerCase())) return post;
        }
        return null;
    }

    public static Post parsePostElement(Element element) {
        Post post = null;
        if (element.getName().toLowerCase().equals("post")) {
            List<RegionModule> regions = Lists.newArrayList();
            if (element.getText() != null) {
                regions.add(new PointRegion(new PointParser(element)));
            } else {
                for (Element e : element.getChildren()) {
                    regions.add(RegionModuleBuilder.getRegion(e));
                }
            }
            String id = element.getAttributeValue("id") == null ? null : element.getAttributeValue("id");
            TeamModule owner = element.getAttributeValue("owner") == null ? null : Teams.getTeamById(element.getAttributeValue("owner")).orNull();
            boolean permanent = Boolean.parseBoolean(element.getAttributeValue("permanent", "false"));
            boolean sequential = Boolean.parseBoolean(element.getAttributeValue("sequential", "false"));
            int pointsRate = Numbers.parseInt(element.getAttributeValue("points-rate", "0"));
            FilterModule pickupFilter = null;
            if (element.getAttributeValue("pickup-filter") != null) {
                pickupFilter = FilterModuleBuilder.getFilter(element.getAttributeValue("pickup-filter"));
            } else if (element.getChild("pickup-filter") != null) {
                pickupFilter = FilterModuleBuilder.getFilter(element.getChild("pickup-filter"));
            }
            int recoverTime = 30;
            if (element.getAttributeValue("recover-time") != null) {
                recoverTime = Strings.timeStringToSeconds(element.getAttributeValue("recover-time"));
            } else if (element.getAttributeValue("return-time") != null) {
                recoverTime = Strings.timeStringToSeconds(element.getAttributeValue("return-time"));
            }
            int respawnTime = Strings.timeStringToSeconds(element.getAttributeValue("respawn-time", "0s"));
            int respawnSpeed = Numbers.parseInt(element.getAttributeValue("respawn-speed", "8"));
            float yaw = (float) Numbers.parseDouble(element.getAttributeValue("yaw", "0"));
            post = new Post(regions, id, owner, permanent, sequential, pointsRate, pickupFilter, recoverTime, respawnTime, respawnSpeed, yaw);
        }
        return post;
    }
}
