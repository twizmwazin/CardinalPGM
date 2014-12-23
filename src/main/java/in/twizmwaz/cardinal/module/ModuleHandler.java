package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.buildHeight.BuildHeightBuilder;
import in.twizmwaz.cardinal.module.modules.friendlyFire.FriendlyFireBuilder;
import in.twizmwaz.cardinal.module.modules.itemRemove.ItemRemoveBuilder;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import in.twizmwaz.cardinal.module.modules.timeLock.TimeLockBuilder;
import in.twizmwaz.cardinal.module.modules.toolRepair.ToolRepairBuilder;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjectiveBuilder;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class ModuleHandler {

    private final JavaPlugin plugin;
    private final GameHandler gameHandler;

    private Set<Module> loaded;
    private Set<GameObjective> objectives;

    public ModuleHandler(JavaPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        this.loaded = null;
    }

    public Set<Module> invokeModules(Match match) {
        Document document = match.getDocument();
        Set<Module> loaded = new HashSet<Module>();
        loaded.addAll(new BuildHeightBuilder().load(match));
        loaded.addAll(new WoolObjectiveBuilder().load(match));
        loaded.addAll(new ItemRemoveBuilder().load(match));
        loaded.addAll(new ToolRepairBuilder().load(match));
        loaded.addAll(new KitBuilder().load(match));
        loaded.addAll(new TimeLockBuilder().load(match));
        loaded.addAll(new FriendlyFireBuilder().load(match));
        this.loaded = loaded;
        Bukkit.getLogger().log(Level.INFO, loaded.size() + " modules loaded.");

        objectives = new HashSet<GameObjective>();
        for (Module loadedModule : loaded) {
            if (loadedModule instanceof GameObjective) {
                objectives.add((GameObjective) loadedModule);
            }
        }
        for (Module module : loaded) {
            plugin.getServer().getPluginManager().registerEvents(module, plugin);
        }
        assignObjectives(match.getTeams());
        return loaded;
    }

    public Set<GameObjective> getConditions() {
        return objectives;
    }

    public void unregisterModules() {
        for (Module module : this.loaded) {
            module.unload();
        }
        this.loaded = null;
        this.objectives = null;
    }

    private void assignObjectives(List<PgmTeam> teams) {
        for (PgmTeam team : teams) {
            Set<GameObjective> objectives = new HashSet<GameObjective>();
            for (GameObjective objective : this.objectives) {
                if (objective.getTeam().equals(team)) {
                    objectives.add(objective);
                }
            }
            team.setObjectives(objectives);
        }
    }


}
