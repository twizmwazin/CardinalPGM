package in.twizmwaz.cardinal.match;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.startTimer.StartTimer;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.util.DomUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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
            this.document = DomUtil.parse(new File(map.getFolder() + "/map.xml"));
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
        return getState() == MatchState.PLAYING;
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        if (state == null) throw new IllegalArgumentException("MatchState cannot be null!");
        this.state = state;
    }

    public Document getDocument() {
        return document;
    }

    public void start(int time) {
        start(time, false);
    }

    public void start(int time, boolean forced) {
        if (state == MatchState.WAITING) {
            StartTimer startTimer = getModules().getModule(StartTimer.class);
            startTimer.setTime(time);
            startTimer.setForced(forced);
            startTimer.setCancelled(false);
            state = MatchState.STARTING;
        }
    }

    public void end(TeamModule team) {
        if (getState() == MatchState.PLAYING) {
            state = MatchState.ENDED;
            Event event = new MatchEndEvent(team == null ? Optional.<TeamModule>absent() : Optional.of(team));
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }

    public void end(Player player) {
        if (getState() == MatchState.PLAYING) {
            state = MatchState.ENDED;
            Bukkit.getServer().getPluginManager().callEvent(new MatchEndEvent(player));
        }
    }

    public ModuleCollection<Module> getModules() {
        return modules;
    }

    public int getNumber() {
        return number;
    }

    public LoadedMap getLoadedMap() {
        return loadedMap;
    }

    public UUID getUuid() {
        return uuid;
    }
}
