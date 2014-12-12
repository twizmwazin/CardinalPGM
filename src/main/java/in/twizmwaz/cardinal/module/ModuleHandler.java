package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.buildHeight.BuildHeightBuilder;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjectiveBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ModuleHandler {

    private final JavaPlugin plugin;
    private final GameHandler gameHandler;

    private List<Module> loaded;
    private List<GameCondition> conditions;

    public ModuleHandler(JavaPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        this.loaded = null;
    }

    public List<Module> invokeModules(Match match) {
        Document document = match.getDocument();
        List<Module> loaded = new ArrayList<Module>();
        if (document.getRootElement().getChildren("maxbuildheight").size() != 0) {
            loaded.addAll(new BuildHeightBuilder().load(match));
        }
        if (document.getRootElement().getChildren("wools").size() != 0) {
            loaded.addAll(new WoolObjectiveBuilder().load(match));
        }

        this.loaded = loaded;
        Bukkit.getLogger().log(Level.INFO, loaded.size() + "modules loaded.");

        conditions = new ArrayList<GameCondition>();
        for (Module loadedModule : loaded) {
            if (loadedModule instanceof GameCondition){
                conditions.add((GameCondition) loadedModule);
            }
        }
        for (Module module : loaded) {
            plugin.getServer().getPluginManager().registerEvents(module, plugin);
        }
        return loaded;
    }

    public List<GameCondition> getConditions() {
        return conditions;
    }

    public void unregisterModules() {
        for (Module module : this.loaded) {
            module.unload();
        }
        this.loaded = null;
        this.conditions = null;
    }


}
