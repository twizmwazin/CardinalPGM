package in.twizmwaz.cardinal.module.modules.ctf.net;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.util.Vector;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

@LoadTime(ModuleLoadTime.LATER)
public class NetBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Net> results = new ModuleCollection<>();
        for (Element flags : match.getDocument().getRootElement().getChildren("flags")) {
            for (Element net : flags.getChildren("net")) {
                results.add(parseNet(net, flags));
            }
            for (Element subFlags : flags.getChildren("flags")) {
                for (Element net : subFlags.getChildren("net")) {
                    results.add(parseNet(net, subFlags, flags));
                }
            }
        }
        return results;
    }

    public static Net parseNet(Element... elements) {
        if (elements[0].getName().equals("net")) {
            String id = elements[0].getAttributeValue("id") == null ? null : elements[0].getAttributeValue("id");
            RegionModule region = RegionModuleBuilder.getAttributeOrChild("region", elements);
            TeamModule owner = Parser.getOrderedAttribute("owner", elements) == null ? null : Teams.getTeamById(Parser.getOrderedAttribute("owner", elements)).orNull();
            int points = Numbers.parseInt(Parser.getOrderedAttribute("points",elements), 0);
            Post post = Flags.getPostById(Parser.getOrderedAttribute("post", elements));

            Set<FlagObjective> flagObjectives = new HashSet<>();
            Set<FlagObjective> rescue = new HashSet<>();
            if (!elements[1].getName().equalsIgnoreCase("flag")) {
                try {
                    String flag = Parser.getOrderedAttribute("flag", elements);
                    if (flag != null) for (String str : flag.split(" ")) flagObjectives.add(Flags.getFlagById(str));
                    String flags = Parser.getOrderedAttribute("flags", elements);
                    if (flags != null) for (String str : flags.split(" ")) flagObjectives.add(Flags.getFlagById(str));
                    flagObjectives.remove(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    flagObjectives.clear();
                }
                if (flagObjectives.size() == 0) flagObjectives.addAll(Flags.getFlags());
            }
            String rescueStr = Parser.getOrderedAttribute("rescue", elements);
            if (rescueStr != null)
                for (String str : rescueStr.split(" ")) flagObjectives.add(Flags.getFlagById(str));

            boolean sticky = Numbers.parseBoolean(Parser.getOrderedAttribute("sticky", elements), true);
            FilterModule captureFilter = FilterModuleBuilder.getAttributeOrChild("capture-filter", "always", elements);
            String denyMessage = Parser.getOrderedAttributeOrChild("deny-message", elements);
            boolean respawnTogether = Numbers.parseBoolean(Parser.getOrderedAttribute("respawn-together", elements), false);
            FilterModule respawnFilter = FilterModuleBuilder.getAttributeOrChild("respawn-filter", "always", elements);
            String respawnMessage = Parser.getOrderedAttributeOrChild("respawn-message", elements);
            String loc = elements[0].getAttributeValue("location");
            Vector location = loc != null ? Parser.parseVector(loc) : region.getCenterBlock().getAlignedVector();

            return new Net(id, region, owner, points, post, flagObjectives, rescue, sticky, captureFilter, denyMessage, respawnTogether, respawnFilter, respawnMessage, location);
        }
        return null;
    }

}
