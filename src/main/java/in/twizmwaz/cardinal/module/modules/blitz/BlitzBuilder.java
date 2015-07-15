package in.twizmwaz.cardinal.module.modules.blitz;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.rage.Rage;
import in.twizmwaz.cardinal.util.Numbers;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.LATE)
public class BlitzBuilder implements ModuleBuilder {

    public ModuleCollection<Blitz> load(Match match) {
        ModuleCollection<Blitz> result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("blitz")) {
            boolean broadcastLives = element.getChild("broadcastLives") == null || Numbers.parseBoolean(element.getChild("broadcastLives").getText());
            int lives = element.getChild("lives") == null ? 1 : Numbers.parseInt(element.getChild("lives").getText());
            String title = element.getChildText("title") == null ? (match.getModules().getModule(Rage.class) != null ? "Blitz: Rage" : "Blitz") : element.getChildText("title");
            result.add(new Blitz(title, broadcastLives, lives));
        }
        return result;
    }

}

