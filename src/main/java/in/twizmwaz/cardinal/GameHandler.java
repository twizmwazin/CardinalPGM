package in.twizmwaz.cardinal;


import in.twizmwaz.cardinal.cycle.Cycle;
import in.twizmwaz.cardinal.cycle.CycleTimer;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.ModuleHandler;
import in.twizmwaz.cardinal.rotation.Rotation;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Created by kevin on 10/31/14.
 */
public class GameHandler {

    private static GameHandler handler;
    private final JavaPlugin plugin;
    private Rotation rotation;
    private UUID matchUUID;
    private World matchWorld;
    private Match match;
    private Cycle cycle;
    private CycleTimer cycleTimer;
    private ModuleHandler moduleHandler;

    public GameHandler(JavaPlugin plugin) throws RotationLoadException {
        this.plugin = plugin;
        handler = this;
        moduleHandler = new ModuleHandler(plugin, this);
        rotation = new Rotation(plugin);
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);
        cycleAndMakeMatch();
        //initialCycle();
    }

    public static GameHandler getGameHandler() {
        return handler;
    }

    public void cycleAndMakeMatch() {
        matchUUID = UUID.randomUUID();
        rotation.move();
        World oldMatchWorld = matchWorld;
        cycle.run();
        this.matchUUID = cycle.getUuid();
        try {
            match.unregister();
        } catch (NullPointerException e) {
        }
        this.match = new Match(this, matchUUID);
        match.setModules(moduleHandler.invokeModules(match));
        Bukkit.getServer().getPluginManager().callEvent(new CycleCompleteEvent(match));
        rotation.move();
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);
        Bukkit.unloadWorld(oldMatchWorld, true);
    }

    public Rotation getRotation() {
        return rotation;
    }

    public World getMatchWorld() {
        return matchWorld;
    }

    public void setMatchWorld(World world) {
        this.matchWorld = world;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public CycleTimer getCycleTimer() {
        return cycleTimer;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public ModuleHandler getModuleHandler() {
        return moduleHandler;
    }

    public boolean startCycleTimer(int seconds) {
        if (this.getMatch().getState() != MatchState.PLAYING) {
            this.cycleTimer = new CycleTimer(this.getCycle(), seconds);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), cycleTimer);
            return true;
        } else return false;
    }
}
