package in.twizmwaz.cardinal.module.modules.tasker;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.TaskedModule;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class TaskerModule implements TaskedModule {
    
    private final Match match;
    
    protected TaskerModule(Match match) {
        this.match = match;
    }
    
    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (match.equals(GameHandler.getGameHandler().getMatch())) {
            for (TaskedModule task : match.getModules().getModules(TaskedModule.class)) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), task, 1);
            }
        }
    }
    
    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        run();
    }
}
