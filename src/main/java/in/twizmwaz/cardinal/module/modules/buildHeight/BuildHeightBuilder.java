package in.twizmwaz.cardinal.module.modules.buildHeight;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.Element;

public class BuildHeightBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection result = new ModuleCollection();
        JavaPlugin plugin = GameHandler.getGameHandler().getPlugin();
        for (Element element : match.getDocument().getRootElement().getChildren("maxbuildheight")) {
            int height = Integer.parseInt(element.getValue());
            result.add(new BuildHeight(plugin, height));
        }
        return result;
    }

}
