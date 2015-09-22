package in.twizmwaz.cardinal.module.modules.ctf;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import in.twizmwaz.cardinal.module.modules.ctf.net.NetBuilder;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.ctf.post.PostBuilder;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.jdom2.Element;

import java.util.List;

@BuilderData(load = ModuleLoadTime.LATE)
public class FlagBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Flag> results = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("flags")) {
            for (Element flag : element.getChildren("flag")) {
                String id = flag.getAttributeValue("id") == null ? null : flag.getAttributeValue("id");
                boolean required = Boolean.parseBoolean(flag.getAttributeValue("required", "true"));
                String name = flag.getAttributeValue("name") == null ? null : flag.getAttributeValue("name");
                DyeColor color = flag.getAttributeValue("color") == null ? null : Parser.parseDyeColor(flag.getAttributeValue("color"));
                boolean show = Boolean.parseBoolean(flag.getAttributeValue("show", "true"));
                Post post;
                if (flag.getAttributeValue("post") != null) {
                    post = PostBuilder.getPost(flag.getAttributeValue("post"));
                } else {
                    post = PostBuilder.parsePostElement(flag.getChild("post"));
                }
                Cardinal.getInstance().getServer().getPluginManager().registerEvents(post, Cardinal.getInstance());
                List<Net> nets = null;
                if (flag.getChildren("net") != null) {
                    nets = Lists.newArrayList();
                    for (Element enet : flag.getChildren("net")) {
                        Net net = NetBuilder.parseNet(enet);
                        nets.add(net);
                        Cardinal.getInstance().getServer().getPluginManager().registerEvents(net, Cardinal.getInstance());
                    }
                }
                TeamModule owner = flag.getAttributeValue("owner") == null ? null : Teams.getTeamById(flag.getAttributeValue("owner")).orNull();
                boolean shared = Boolean.parseBoolean(flag.getAttributeValue("shared", "false"));
                String carryMessage = flag.getAttributeValue("carry-message") == null ? null : flag.getAttributeValue("carry-message");
                int points = Numbers.parseInt(flag.getAttributeValue("points", "0"));
                int pointsRate = Numbers.parseInt(flag.getAttributeValue("points-rate", "0"));
                FilterModule pickupFilter;
                if (flag.getAttributeValue("pickup-filter") != null) {
                    pickupFilter = FilterModuleBuilder.getFilter(flag.getAttributeValue("pickup-filter"));
                } else if (flag.getChild("pickup-filter") != null) {
                    pickupFilter = FilterModuleBuilder.getFilter(flag.getChild("pickup-filter"));
                } else {
                    pickupFilter = post.getPickupFilter();
                }
                FilterModule captureFilter = null;
                if (flag.getAttributeValue("pickup-filter") != null) {
                    captureFilter = FilterModuleBuilder.getFilter(flag.getAttributeValue("capture-filter"));
                } else if (flag.getChild("pickup-filter") != null) {
                    captureFilter = FilterModuleBuilder.getFilter(flag.getChild("capture-filter"));
                } else {
                    for (Net net : nets) {
                        if (net.getCaptureFilter() != null) captureFilter = net.getCaptureFilter();
                    }
                }
                Kit pickupKit = null;
                if (flag.getAttributeValue("pickup-kit") != null) {
                    pickupKit = Kit.getKitByName(flag.getAttributeValue("pickup-kit"));
                } else if (flag.getChild("pickup-kit") != null) {
                    pickupKit = KitBuilder.getKit(flag.getChild("pickup-kit"));
                }
                Kit dropKit = null;
                if (flag.getAttributeValue("drop-kit") != null) {
                    dropKit = Kit.getKitByName(flag.getAttributeValue("drop-kit"));
                } else if (flag.getChild("drop-kit") != null) {
                    dropKit = KitBuilder.getKit(flag.getChild("drop-kit"));
                }
                Kit carryKit = null;
                if (flag.getAttributeValue("carry-kit") != null) {
                    carryKit = Kit.getKitByName(flag.getAttributeValue("carry-kit"));
                } else if (flag.getChild("carry-kit") != null) {
                    carryKit = KitBuilder.getKit(flag.getChild("carry-kit"));
                }
                boolean dropOnWater = Boolean.parseBoolean(flag.getAttributeValue("drop-on-water", "true"));
                Flag fflag = new Flag(id, required, name, color, show, post, nets, owner, shared, carryMessage, points, pointsRate, pickupFilter, captureFilter, pickupKit, dropKit, carryKit, dropOnWater);
                results.add(fflag);

                // DEBUG
                for (String s : fflag.debug()) {
                    Bukkit.getLogger().info(s);
                }
            }
        }
        return results;
    }
}
