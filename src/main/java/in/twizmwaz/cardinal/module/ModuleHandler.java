package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.buildHeight.BuildHeight;
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

    public ModuleHandler(JavaPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        this.loaded = null;
    }

    public List<Module> invokeModules(Document document) {
        List<Module> loaded = new ArrayList<Module>();
        if (document.getRootElement().getChildren("maxbuildheight").size() != 0) {
            loaded.add(new BuildHeight(document));
        }
        this.loaded = loaded;
        Bukkit.getLogger().log(Level.INFO, loaded.size() + "modules loaded.");
        return loaded;
    }

    public void unregisterModules() {
        Bukkit.broadcastMessage("unload started");
        for (Module module : this.loaded) {
            module.unload();
        }
        this.loaded = null;
    }


}
