package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.buildHeight.BuildHeightBuilder;
import in.twizmwaz.cardinal.module.modules.difficulty.MapDifficultyBuilder;
import in.twizmwaz.cardinal.module.modules.disableDamage.DisableDamageBuilder;
import in.twizmwaz.cardinal.module.modules.friendlyFire.FriendlyFireBuilder;
import in.twizmwaz.cardinal.module.modules.gamerules.GamerulesBuilder;
import in.twizmwaz.cardinal.module.modules.hunger.HungerBuilder;
import in.twizmwaz.cardinal.module.modules.itemRemove.ItemRemoveBuilder;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import in.twizmwaz.cardinal.module.modules.motd.MOTDBuilder;
import in.twizmwaz.cardinal.module.modules.projectiles.ProjectilesBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.timeLock.TimeLockBuilder;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTrackerBuilder;
import in.twizmwaz.cardinal.module.modules.toolRepair.ToolRepairBuilder;
import in.twizmwaz.cardinal.module.modules.visibility.VisibilityBuilder;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjectiveBuilder;
import in.twizmwaz.cardinal.module.modules.worldFreeze.WorldFreezeBuilder;
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

    Set<ModuleBuilder> builders = new HashSet<>();
    private Set<Module> loaded;
    private Set<GameObjective> objectives;

    public ModuleHandler(JavaPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        this.loaded = null;
        builders = new HashSet<>();
        builders.add(new RegionModuleBuilder());
        builders.add(new BuildHeightBuilder());
        builders.add(new WoolObjectiveBuilder());
        builders.add(new ItemRemoveBuilder());
        builders.add(new ToolRepairBuilder());
        builders.add(new DisableDamageBuilder());
        builders.add(new GamerulesBuilder());
        builders.add(new KitBuilder());
        builders.add(new TimeLockBuilder());
        builders.add(new FriendlyFireBuilder());
        builders.add(new HungerBuilder());
        builders.add(new MapDifficultyBuilder());
        builders.add(new HungerBuilder());
        builders.add(new ProjectilesBuilder());
        builders.add(new TntTrackerBuilder());
        builders.add(new VisibilityBuilder());
        builders.add(new MOTDBuilder());
        builders.add(new WorldFreezeBuilder());
    }

    public Set<Module> invokeModules(Match match) {
        Document document = match.getDocument();
        Set<Module> loaded = new HashSet<Module>();
        for (ModuleBuilder builder : builders) {
            loaded.addAll(builder.load(match));
        }
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

    public Set<Module> getModules() {
        return loaded;
    }
    
    public Set<Module> getModule(String name) {
        Set<Module> results = new HashSet<>();
        for (Module module : this.getModules()) {
            if (module.getClass().getName().equalsIgnoreCase(name)) {
                results.add(module);
            }
        }
        return results;
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
