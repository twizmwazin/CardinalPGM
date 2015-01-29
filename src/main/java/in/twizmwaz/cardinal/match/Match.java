package in.twizmwaz.cardinal.match;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.match.util.StartTimer;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleFactory;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.mapInfo.Info;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.DomUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Match {

    private static int matchNumber = 1;

    private final JavaPlugin plugin;
    private final GameHandler handler;
    private final UUID uuid;
    private final ModuleFactory factory;
    private final ModuleCollection<Module> modules;

    private int number;
    private MatchState state;
    private Document document;
    private StartTimer startTimer;

    public Match(GameHandler handler, UUID id) {
        this.plugin = handler.getPlugin();
        this.uuid = id;
        this.handler = handler;
        this.modules = new ModuleCollection<>();
        this.factory = new ModuleFactory(this);
        try {
            this.document = DomUtils.parse(new File("matches/" + this.uuid.toString() + "/map.xml"));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        this.startTimer = new StartTimer(this, 30);
        this.state = MatchState.WAITING;
        this.number = matchNumber;
        matchNumber ++;
    }

    public void registerModules() {
        for (ModuleLoadTime time : ModuleLoadTime.getOrdered()) {
            for (Module module : factory.build(time)) {
                modules.add(module);
                plugin.getServer().getPluginManager().registerEvents(module, plugin);
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

    public Info getMapInfo() {
        return Info.getMapInfo();
    }

    public Document getDocument() {
        return document;
    }

    public StartTimer getStartTimer() {
        return startTimer;
    }

    public void start(int time) {
        if (state == MatchState.WAITING) {
            startTimer.setTime(time);
            startTimer.setCancelled(false);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GameHandler.getGameHandler().getPlugin(), startTimer);
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
}
