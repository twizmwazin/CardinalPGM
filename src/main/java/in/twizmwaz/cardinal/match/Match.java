package in.twizmwaz.cardinal.match;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleFactory;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.startTimer.StartTimer;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.util.DomUtils;
import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Match {

    private static int matchNumber = 1;

    private final UUID uuid;
    private final LoadedMap loadedMap;
    private final ModuleCollection<Module> modules;

    private int number;
    private MatchState state;
    private Document document;

    public Match(UUID id, LoadedMap map) {
        this.uuid = id;
        this.modules = new ModuleCollection<>();
        try {
            this.document = DomUtils.parse(new File(map.getFolder() + "/map.xml"));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        this.state = MatchState.WAITING;
        this.loadedMap = map;
        this.number = matchNumber;
        matchNumber++;
    }

    public void registerModules() {
        for (ModuleLoadTime time : ModuleLoadTime.getOrdered()) {
            for (Module module : GameHandler.getGameHandler().getModuleFactory().build(this, time)) {
                modules.add(module);
                Cardinal.getInstance().getServer().getPluginManager().registerEvents(module, Cardinal.getInstance());
            }
        }
    }

    public void unregisterModules() {
        modules.unregisterAll();
    }

    public Match getMatch() {
        return this;
    }

    public boolean isRunning() {
        return state.equals(MatchState.PLAYING);
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;
    }

    public Document getDocument() {
        return document;
    }

    public void start(int time) {
        if (state == MatchState.WAITING) {
            StartTimer startTimer = getModules().getModule(StartTimer.class);
            startTimer.setTime(time);
            startTimer.setCancelled(false);
            state = MatchState.STARTING;
        }
    }

    public void end(TeamModule team) {
        if (getState() == MatchState.PLAYING) {
            state = MatchState.ENDED;
            Bukkit.getServer().getPluginManager().callEvent(new MatchEndEvent(team));
        }
    }

    public ModuleCollection<Module> getModules() {
        return modules;
    }

    public int getNumber() {
        return number;
    }

    public int getPriorityTimeLimit() {
        int scoreTime = ScoreModule.getTimeLimit();
        int blitzTime = Blitz.getTimeLimit();
        if (scoreTime != 0)
            if (blitzTime != 0)
                return (scoreTime < blitzTime ? scoreTime : blitzTime);
            else return scoreTime;
        else if (blitzTime != 0)
            return blitzTime;
        return 0;
    }

    public LoadedMap getLoadedMap() {
        return loadedMap;
    }
}
