package in.twizmwaz.cardinal.module.modules.blitz;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.util.TimeUtils;
import org.jdom2.Element;

public class BlitzBuilder implements ModuleBuilder {

    public ModuleCollection load(Match match) {
        ModuleCollection result = new ModuleCollection();
        for (Element element : match.getDocument().getRootElement().getChildren("blitz")) {
            boolean broadcastLives = false;
            if (element.getChild("broadcastLives").getText() != null) {
                broadcastLives = Boolean.parseBoolean(element.getChild("broadcastLives").getText());
            }
            int lives = 1;
            if (element.getChild("lives").getText() != null) {
                lives = Integer.parseInt(element.getChild("lives").getText());
            }
            int time = 0;
            if (element.getChild("time").getText() != null) {
                time = TimeUtils.timePeriodsToSeconds(element.getChild("time").getText());
            }
            String title = null;
            if (element.getChild("title").getText() != null) {
                title = element.getChild("title").getText();
            }
            result.add(new Blitz(title, broadcastLives, lives, time));
        }
        return result;
    }

}

