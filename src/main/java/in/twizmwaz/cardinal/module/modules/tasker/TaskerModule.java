package in.twizmwaz.cardinal.module.modules.tasker;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.broadcasts.BroadcastModule;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class TaskerModule implements TaskedModule {
    
    private final Match match;
    private final JavaPlugin plugin;
    
    protected TaskerModule(Match match) {
        this.match = match;
        this.plugin = GameHandler.getGameHandler().getPlugin();
    }
    
    @Override
    public void unload() {
    }

    @Override
    public void run() {
        for (TaskedModule task : match.getModules().getModules(TaskedModule.class)) {
            if (match.isRunning()) Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, 1);
        }
    }
    
    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        run();
    }
}
