package in.twizmwaz.cardinal.module.modules.tutorial;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TutorialBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<Tutorial> results = new ModuleCollection<>();
        for (Element tutorial : match.getDocument().getRootElement().getChildren("tutorial")) {
            String title = null;
            List<String> line = new ArrayList<>();
            RegionModule teleport = null;
            if (tutorial.getChild("stage").getAttribute("title") != null) {
                title = tutorial.getChild("stage").getAttributeValue("title");
                for (Element lineElement : tutorial.getChild("stage").getChildren("message")) {
                    if (lineElement.getText() != null) {
                        line.add(lineElement.getText());
                    }
                }

                if (tutorial.getChild("stage").getChild("teleport") != null) {
                    teleport = RegionModuleBuilder.getRegion(tutorial.getChild("stage").getChild("teleport"));
                }
            }
            results.add(new Tutorial(title, line, teleport));
        }
        return results;
    }

}