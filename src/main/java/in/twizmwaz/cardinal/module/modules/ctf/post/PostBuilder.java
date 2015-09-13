package in.twizmwaz.cardinal.module.modules.ctf.post;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.parsers.PointParser;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.jdom2.Element;

public class PostBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Post> results = new ModuleCollection<>();
        for (Element flags : match.getDocument().getRootElement().getChildren("flags")) {
            for (Element post : flags.getChildren("post")) {
                results.add(parsePostElement(post));
            }
            for (Element subFlags : flags.getChildren("flags")) {
                for (Element post : subFlags.getChildren("post")) {
                    results.add(parsePostElement(post));
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
            PointRegion point = new PointRegion(new PointParser(element));
            String id = element.getAttributeValue("id") == null ? null : element.getAttributeValue("id");
            TeamModule owner = element.getAttributeValue("owner") == null ? null : Teams.getTeamById(element.getAttributeValue("owner")).orNull();
            boolean permanent = Boolean.parseBoolean(element.getAttributeValue("permanent", "false"));
            boolean sequential = Boolean.parseBoolean(element.getAttributeValue("sequential", "false"));
            int pointsRate = Numbers.parseInt(element.getAttributeValue("points-rate", "0"));
            FilterModule pickupFilter;
            if (element.getAttributeValue("pickup-filter") != null) {
                pickupFilter = FilterModuleBuilder.getFilter(element.getAttributeValue("pickup-filter"));
            } else {
                pickupFilter = FilterModuleBuilder.getFilter(element.getChild("pickup-filter"));
            }
            int recoverTime = Strings.timeStringToSeconds(element.getAttributeValue("return-time", "30s"));
            int respawnTime = Strings.timeStringToSeconds(element.getAttributeValue("respawn-time", "0s"));
            int respawnSpeed = Numbers.parseInt(element.getAttributeValue("respawn-speed", "8"));
            float yaw = (float) Numbers.parseDouble(element.getAttributeValue("yaw", "0"));
            post = new Post(point, id, owner, permanent, sequential, pointsRate, pickupFilter, recoverTime, respawnTime, respawnSpeed, yaw);

            // DEBUG
            for (String s : post.debug()) {
                Bukkit.getLogger().info(s);
            }
        }
        return post;
    }
}
