package in.twizmwaz.cardinal.module.modules.tutorial;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import org.bukkit.ChatColor;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class TutorialBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<Tutorial> result = new ModuleCollection<>();
        Stage prefix = null;
        Stage suffix = null;

        List<Stage> stages = new ArrayList<>();
        for (Element tutorial : match.getDocument().getRootElement().getChildren("tutorial")) {
            for (Element stage : tutorial.getChildren("stage")) {
                stages.add(parseStage(stage));
            }

            if (prefix == null) {
                Element prefixElement = tutorial.getChild("prefix");
                if (prefixElement != null) {
                    prefix = parseStage(prefixElement.getChild("stage"));
                    suffix = parseStage(tutorial.getChild("suffix").getChild("stage"));
                }
            }
        }
        if ((prefix != null && suffix != null) || !stages.isEmpty()) {
            result.add(new Tutorial(prefix, stages, suffix));
        }
        return result;
    }

    private Stage parseStage(Element stage) {
        String title = ChatColor.translateAlternateColorCodes('`', stage.getAttributeValue("title"));
        List<String> lines = new ArrayList<>();
        RegionModule region = null;
        for (Element line : stage.getChild("message").getChildren("line")) {
            lines.add(ChatColor.translateAlternateColorCodes('`', line.getValue()));
        }
        Element teleport = stage.getChild("teleport");
        if (teleport != null) {
            region = RegionModuleBuilder.getRegion(teleport.getChildren().get(0));
        }
        return new Stage(title, lines, region);
    }
}
