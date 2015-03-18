package in.twizmwaz.cardinal;

import in.twizmwaz.cardinal.cycle.Cycle;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleFactory;
import in.twizmwaz.cardinal.module.modules.cycleTimer.CycleTimerModule;
import in.twizmwaz.cardinal.rotation.Rotation;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;

import java.lang.ref.WeakReference;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class GameHandler {

    private static GameHandler handler;
    private final ModuleFactory moduleFactory;
    private Rotation rotation;
    private WeakReference<World> matchWorld;
    private Match match;
    private Cycle cycle;

    public GameHandler() throws RotationLoadException {
        handler = this;
        this.moduleFactory = new ModuleFactory();
        rotation = new Rotation();
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);
        cycleAndMakeMatch();
    }

    public static GameHandler getGameHandler() {
        return handler;
    }

    public void cycleAndMakeMatch() {
        rotation.move();
        World oldMatchWorld = matchWorld == null ? null: matchWorld.get();
        cycle.run();
        if (match != null) match.unregisterModules();
        this.match = new Match(cycle.getUuid(), cycle.getMap());
        this.match.registerModules();
        Bukkit.getLogger().info("[CardinalPGM] " + this.match.getModules().size() + " modules loaded.");
        Bukkit.getServer().getPluginManager().callEvent(new CycleCompleteEvent(match));
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);
        Bukkit.unloadWorld(oldMatchWorld, true);
    }

    public Rotation getRotation() {
        return rotation;
    }

    public World getMatchWorld() {
        return matchWorld.get();
    }

    public void setMatchWorld(World world) {
        matchWorld = new WeakReference<>(world);
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

    public JavaPlugin getPlugin() {
        return Cardinal.getInstance();
    }

    public ModuleFactory getModuleFactory() {
        return moduleFactory;
    }
}
