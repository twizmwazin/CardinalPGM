package in.twizmwaz.cardinal.module.modules.monumentModes;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Strings;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.jdom2.Element;

@LoadTime(ModuleLoadTime.LATEST)
public class MonumentModesBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MonumentModes> load(Match match) {
        ModuleCollection<MonumentModes> results = new ModuleCollection<>();
        for (Element modes : match.getDocument().getRootElement().getChildren("modes")) {
            for (Element mode : modes.getChildren("mode")) {
                int after = Strings.timeStringToSeconds(mode.getAttributeValue("after"));
                int showBefore = (after < 60 ? after : 60);
                if (mode.getAttributeValue("show-before") != null) {
                    showBefore = Strings.timeStringToSeconds(mode.getAttributeValue("show-before"));
                }
                Pair<Material, Integer> material = Parser.parseMaterial(mode.getAttributeValue("material"));
                String name = material.getKey().name().replaceAll("_", " ") + " MODE";
                if (mode.getAttributeValue("name") != null) {
                    name = ChatColor.translateAlternateColorCodes('`', mode.getAttributeValue("name"));
                }
                results.add(new MonumentModes(after, material, name, showBefore));
            }
        }
        boolean core = match.getModules().getModules(CoreObjective.class).size() > 0;
        if (results.size() == 0 && core) {
            for (CoreObjective coreObjective : match.getModules().getModules(CoreObjective.class)) {
                coreObjective.setChangesModes(true);
            }
            results.add(new MonumentModes(900, new ImmutablePair<>(Material.GOLD_BLOCK, 0), "GOLD CORE MODE", 60));
            results.add(new MonumentModes(1200, new ImmutablePair<>(Material.GLASS, 0), "GLASS CORE MODE", 60));
        }
        return results;
    }
}