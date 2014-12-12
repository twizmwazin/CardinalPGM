package in.twizmwaz.cardinal;


import in.twizmwaz.cardinal.cycle.Cycle;
import in.twizmwaz.cardinal.cycle.CycleTimer;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.ModuleHandler;
import in.twizmwaz.cardinal.rotation.Rotation;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

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

    public GameHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        handler = this;
        moduleHandler = new ModuleHandler(plugin, this);
        rotation = new Rotation(new File("rotation.txt"));
        initialCycle();
    }

    public static GameHandler getGameHandler() {
        return handler;
    }

    private void initialCycle() {
        try {
            this.cycle = new Cycle(rotation.getEntry(0), UUID.randomUUID(), this);
            this.cycle.run();
            rotation.move();
        } catch (RotationLoadException ex) {
            Bukkit.getLogger().log(Level.WARNING, ex.getMessage());
        }
        this.matchUUID = cycle.getUuid();
        this.match = new Match(this, matchUUID);
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);


    }

    public void cycleAndMakeMatch() {
        matchUUID = UUID.randomUUID();
        rotation.move();
        World oldMatchWorld = matchWorld;
        cycle.run();
        Bukkit.unloadWorld(oldMatchWorld, true);
        this.matchUUID = cycle.getUuid();
        match.unregister();
        this.match = new Match(this, matchUUID);
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);
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
