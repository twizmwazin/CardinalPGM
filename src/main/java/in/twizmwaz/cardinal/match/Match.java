package in.twizmwaz.cardinal.match;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.Chat;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.match.util.StartTimer;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleFactory;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.mapInfo.Info;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.DomUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Match {

    private final JavaPlugin plugin;
    private final GameHandler handler;
    private final UUID uuid;
    private final ModuleFactory factory;
    private final ModuleCollection<Module> modules;

    private MatchState state;
    private Document document;
    private StartTimer startTimer;
    private Set<Listener> listeners = new HashSet<Listener>(); //TO DO:these need to go

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
        listeners.add(new Chat(plugin));
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
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
        modules.unregisterAll();
    }

    public Match getMatch() {
        return this;
    }

    public boolean isRunning() {
        return state == MatchState.PLAYING;
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
}
