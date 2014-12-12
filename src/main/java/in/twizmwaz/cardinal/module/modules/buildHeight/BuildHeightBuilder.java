package in.twizmwaz.cardinal.module.modules.buildHeight;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BuildHeightBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> result = new ArrayList<Module>();
        JavaPlugin plugin = GameHandler.getGameHandler().getPlugin();
        int height = Integer.parseInt(match.getDocument().getRootElement().getChild("maxbuildheight").getValue());
        result.add(new BuildHeight(plugin, height));
        return result;
    }

}
