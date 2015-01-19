package in.twizmwaz.cardinal.match.listeners;

import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class ObjectiveListener implements Listener {

    private final Match match;

    private Set<GameObjective> conditions;

    public ObjectiveListener(JavaPlugin plugin, Match match) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.match = match;
        this.conditions = new HashSet<>();
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        for (TeamModule team : TeamUtils.getTeams()) {
            for (GameObjective condition : TeamUtils.getShownObjectives(team)) {
                if (!condition.isComplete() && !condition.equals(event.getObjective())) return;
            }
            if (TeamUtils.getShownObjectives(team).size() == 0) return;
            match.end(team);
        }
    }
}
