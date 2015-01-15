package in.twizmwaz.cardinal.match.listeners;

import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.teams.PgmTeam;
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
        PgmTeam workingTeam = event.getObjective().getTeam();
        Set<GameObjective> teamObjectives = event.getObjective().getTeam().getObjectives();
        for (GameObjective condition : teamObjectives) {
            if (!condition.isComplete() && !condition.equals(event.getObjective())) return;
        }
        match.end(workingTeam);
    }
}
