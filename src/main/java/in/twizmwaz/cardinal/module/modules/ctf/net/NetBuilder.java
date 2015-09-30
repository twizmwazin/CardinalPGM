package in.twizmwaz.cardinal.module.modules.ctf.net;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.ctf.post.PostBuilder;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Teams;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

@BuilderData(load = ModuleLoadTime.LATE)
public class NetBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Net> results = new ModuleCollection<>();
        for (Element flags : match.getDocument().getRootElement().getChildren("flags")) {
            for (Element net : flags.getChildren("net")) {
                results.add(parseNet(net));
            }
            for (Element subFlags : flags.getChildren("flags")) {
                for (Element net : subFlags.getChildren("net")) {
                    results.add(parseNet(net));
                }
            }
        }
        return results;
    }

    public static Net parseNet(Element element) {
        Net net = null;
        if (element.getName().equals("net")) {
            String id = element.getAttributeValue("id") == null ? null : element.getAttributeValue("id");
            RegionModule region;
            if (element.getAttributeValue("region") != null) {
                region = RegionModuleBuilder.getRegion(element.getAttributeValue("region"));
            } else {
                region = RegionModuleBuilder.getRegion(element.getChild("region"));
            }
            TeamModule owner = element.getAttributeValue("owner") == null ? null : Teams.getTeamById(element.getAttributeValue("owner")).orNull();
            int points = Numbers.parseInt(element.getAttributeValue("points", "0"));
            Post post = element.getAttributeValue("post") == null ? null : PostBuilder.getPost(element.getAttributeValue("post"));
            Set<FlagObjective> flagObjectives = new HashSet<>();
            Set<FlagObjective> rescue = new HashSet<>();
            if (element.getAttributeValue("flag") != null || element.getAttributeValue("flags") != null) {
                String flags = element.getAttributeValue("flag") != null ? element.getAttributeValue("flag") : element.getAttributeValue("flags");
                for (String str : flags.split(" ")) {
                    try {
                        flagObjectives.add(Flags.getFlagById(str));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                flagObjectives.addAll(GameHandler.getGameHandler().getMatch().getModules().getModules(FlagObjective.class));
            }
            if (element.getAttributeValue("rescue") != null) {
                for (String str : element.getAttributeValue("rescue").split(" ")) {
                    try {
                        rescue.add(Flags.getFlagById(str));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            boolean sticky = Boolean.parseBoolean(element.getAttributeValue("sticky", "true"));
            FilterModule captureFilter = null;
            if (element.getAttributeValue("capture-filter") != null) {
                captureFilter = FilterModuleBuilder.getFilter(element.getAttributeValue("capture-filter"));
            } else if (element.getChild("capture-filter") != null) {
                captureFilter = FilterModuleBuilder.getFilter(element.getChild("capture-filter"));
            }
            String denyMessage = element.getAttributeValue("deny-message") == null ? null : element.getAttributeValue("deny-message");
            boolean respawnTogether = Boolean.parseBoolean(element.getAttributeValue("respawn-together", "false"));
            FilterModule respawnFilter = null;
            if (element.getAttributeValue("respawn-filter") != null) {
                respawnFilter = FilterModuleBuilder.getFilter(element.getAttributeValue("respawn-filter"));
            } else if (element.getChild("respawn-filter") != null) {
                respawnFilter = FilterModuleBuilder.getFilter(element.getChild("respawn-filter"));
            }
            String respawnMessage = element.getAttributeValue("respawn-message") == null ? null : element.getAttributeValue("respawn-message");
            net = new Net(id, region, owner, points, post, flagObjectives, rescue, sticky, captureFilter, denyMessage, respawnTogether, respawnFilter, respawnMessage);
        }
        return net;
    }
}
