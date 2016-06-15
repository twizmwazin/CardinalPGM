package in.twizmwaz.cardinal.module.modules.ctf;

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
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import in.twizmwaz.cardinal.module.modules.kit.KitNode;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
import in.twizmwaz.cardinal.module.modules.proximity.ProximityInfo;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.block.Banner;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@BuilderData(load = ModuleLoadTime.LATE)
public class FlagBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<Module> results = new ModuleCollection<>();
        for (Element flags : match.getDocument().getRootElement().getChildren("flags")) {
            for (Element flag : flags.getChildren("flag")) {
                results.addAll(getFlag(flag, flags));
            }
            for (Element flags2 : flags.getChildren("flags")) {
                for (Element flag : flags2.getChildren("flag")) {
                    results.addAll(getFlag(flag, flags2, flags));
                }
            }
        }
        return results;
    }

    private ModuleCollection<? extends Module> getFlag(Element... elements) {
        ModuleCollection<Module> result =  new ModuleCollection<>();
        String id = elements[0].getAttributeValue("id");
        boolean required = Numbers.parseBoolean(Parser.getOrderedAttribute("required", elements), true);
        String name = elements[0].getAttributeValue("name");
        boolean show = Numbers.parseBoolean(Parser.getOrderedAttribute("show", elements), true);
        Post post = Flags.getPostById(Parser.getOrderedAttribute("post", elements));
        if (post == null) post = PostBuilder.parsePostElement(elements[0].getChild("post"));
        result.add(post);
        Set<Net> nets = new HashSet<>();
        if (elements[0].getChildren("net").size() > 0) {
            for (Element netEl : elements[0].getChildren("net")) {
                Net net = NetBuilder.parseNet(Parser.addElement(netEl, elements));
                nets.add(net);
                result.add(net);
            }
        }
        TeamModule owner = Parser.getOrderedAttribute("owner", elements) == null ? null : Teams.getTeamById(Parser.getOrderedAttribute("owner", elements)).orNull();
        boolean shared = Numbers.parseBoolean(Parser.getOrderedAttribute("shared", elements), false);
        DyeColor color = Parser.getOrderedAttribute("color", elements) == null ? ((Banner)post.getInitialBlock().getState()).getBaseColor() : Parser.parseDyeColor(Parser.getOrderedAttribute("color", elements));
        ChatColor chatColor = MiscUtil.convertBannerColorToChatColor(color);
        String carryMessage = ChatColor.AQUA + "" + ChatColor.BOLD + "You are carrying " + chatColor + ChatColor.BOLD + name;
        if (Parser.getOrderedAttributeOrChild("carry-message", elements) != null) carryMessage = ChatColor.translateAlternateColorCodes('`', Parser.getOrderedAttributeOrChild("carry-message", elements));
        int points = Numbers.parseInt(Parser.getOrderedAttribute("points", elements), 0);
        int pointsRate = Numbers.parseInt(Parser.getOrderedAttribute("points-rate", elements), 0);
        FilterModule pickupFilter = FilterModuleBuilder.getAttributeOrChild("pickup-filter", post.getPickupFilter(), elements);
        FilterModule dropFilter = FilterModuleBuilder.getAttributeOrChild("drop-filter", post.getPickupFilter(), elements);
        FilterModule captureFilter = FilterModuleBuilder.getAttributeOrChild("capture-filter", nets.size() > 0 ? nets.iterator().next().getCaptureFilter() : FilterModuleBuilder.getFilter("always"), elements);
        KitNode pickupKit = getKitOrChild("pickup-kit", result, elements);
        KitNode dropKit = getKitOrChild("drop-kit", result, elements);
        KitNode carryKit = getKitOrChild("carry-kit", result, elements);
        boolean dropOnWater = Numbers.parseBoolean(Parser.getOrderedAttribute("drop-on-water", elements), true);
        boolean beam = Numbers.parseBoolean(Parser.getOrderedAttribute("beam", elements), true);

        String flagProximityMetric = Parser.getOrderedAttribute("flagproximity-metric", elements);
        Boolean flagProximityHorizontal = Numbers.parseBoolean(Parser.getOrderedAttribute("flagproximity-horizontal", elements), false);
        ProximityInfo flagProximityInfo = new ProximityInfo(post.getInitialBlock().getLocation().toVector(), flagProximityHorizontal, false,
            flagProximityMetric == null ? GameObjectiveProximityHandler.ProximityMetric.CLOSEST_KILL : GameObjectiveProximityHandler.ProximityMetric.getByName(flagProximityMetric));
    
        String netProximityMetric = Parser.getOrderedAttribute("netproximity-metric", elements);
        Boolean netProximityHorizontal = Numbers.parseBoolean(Parser.getOrderedAttribute("netproximity-horizontal", elements), false);
        ProximityInfo netProximityInfo = new ProximityInfo(null, netProximityHorizontal, true,
                netProximityMetric == null ? GameObjectiveProximityHandler.ProximityMetric.CLOSEST_PLAYER : GameObjectiveProximityHandler.ProximityMetric.getByName(netProximityMetric));

        Map<String, GameObjectiveProximityHandler> flagProximityHandlers = new HashMap<>();
        Map<String, GameObjectiveProximityHandler> netProximityHandlers = new HashMap<>();
        for (TeamModule offender : Teams.getTeams()) {
            if (offender.isObserver() || offender.equals(owner) || !pickupFilter.evaluate(offender).equals(FilterState.ALLOW)) continue;
            GameObjectiveProximityHandler flagProximityHandler = new GameObjectiveProximityHandler(offender, flagProximityInfo);
            GameObjectiveProximityHandler netProximityHandler = new GameObjectiveProximityHandler(offender, netProximityInfo);
            flagProximityHandlers.put(offender.getId(), flagProximityHandler);
            netProximityHandlers.put(offender.getId(), netProximityHandler);
            result.add(flagProximityHandler);
            result.add(netProximityHandler);
        }
        result.add(new FlagObjective(id, required, name, color, chatColor, show, post, owner, shared, carryMessage, points, pointsRate, pickupFilter, dropFilter, captureFilter, pickupKit, dropKit, carryKit, dropOnWater, beam, nets, flagProximityHandlers, netProximityHandlers));

        return result;
    }

    private KitNode getKitOrChild(String name, ModuleCollection<Module> results, Element... elements) {
        if (Parser.getOrderedAttribute(name, elements) != null) {
            return KitNode.getKitByName(Parser.getOrderedAttribute(name, elements));
        } else if (elements[0].getChild(name) != null) {
            KitNode kitNode = KitBuilder.getKit(elements[0].getChild(name));
            for (Kit kit : kitNode.getKits()) results.add(kit);
            return kitNode;
        }
        return null;
    }

}
