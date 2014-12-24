package in.twizmwaz.cardinal.match;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.data.MapInfo;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.match.listeners.*;
import in.twizmwaz.cardinal.match.util.StartTimer;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleHandler;
import in.twizmwaz.cardinal.teams.PgmTeam;
import in.twizmwaz.cardinal.teams.PgmTeamBuilder;
import in.twizmwaz.cardinal.util.DomUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Match {

    private final JavaPlugin plugin;
    private final GameHandler handler;
    private final ModuleHandler moduleHandler;
    private final UUID uuid;
    private final Set<Module> modules;

    private MatchState state;
    private Document document;
    private MapInfo mapInfo;
    private Scoreboard scoreboard;
    private List<PgmTeam> teams;
    private StartTimer startTimer;

    private Set<Listener> listeners = new HashSet<Listener>();

    public Match(GameHandler handler, UUID id) {
        this.plugin = handler.getPlugin();
        this.uuid = id;
        this.handler = handler;
        this.moduleHandler = handler.getModuleHandler();
        try {
            this.document = DomUtil.parse(new File("matches/" + this.uuid.toString() + "/map.xml"));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mapInfo = new MapInfo(document);

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        PgmTeamBuilder teamBuilder = new PgmTeamBuilder(this);
        teamBuilder.run();
        teams = teamBuilder.getTeams();

        this.modules = moduleHandler.invokeModules(this);

        mapInfo = new MapInfo(document);
        this.state = MatchState.WAITING;
        listeners.add(new ConnectionListener(plugin, this));
        listeners.add(new MatchListener(plugin, this));
        listeners.add(new TeamListener(plugin, this));
        listeners.add(new ObserverListener(plugin, this));
        listeners.add(new ObjectiveListener(plugin, this));

        Bukkit.getServer().getPluginManager().callEvent(new CycleCompleteEvent(this));

    }

    public void unregister() {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
        moduleHandler.unregisterModules();
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

    public MapInfo getMapInfo() {
        return mapInfo;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Document getDocument() {
        return document;
    }

    public StartTimer getStartTimer() {
        return startTimer;
    }

    public void start(int time) {
        if (state == MatchState.WAITING) {
            this.startTimer = new StartTimer(GameHandler.getGameHandler(), time);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GameHandler.getGameHandler().getPlugin(), startTimer);
            state = MatchState.STARTING;
        }
    }

    public void end(PgmTeam team) {
        if (getState() == MatchState.PLAYING) {
            state = MatchState.ENDED;
            Bukkit.getServer().getPluginManager().callEvent(new MatchEndEvent(team));
        }
    }

    public PgmTeam getTeam(Player player) {
        for (PgmTeam team : teams) {
            if (team.hasPlayer(player)) return team;
        }
        return null;
    }

    public PgmTeam getTeamByName(String string) {
        for (PgmTeam team : this.teams) {
            if (team.getName().toLowerCase().startsWith(string.toLowerCase())) return team;
        }
        return null;
    }

    public PgmTeam getTeamById(String string) {
        for (PgmTeam team : this.teams) {
            if (team.getId().toLowerCase().startsWith(string.toLowerCase())) return team;
        }
        return null;
    }

    public PgmTeam getTeamWithFewestPlayers() {
        PgmTeam result = null;
        List<Integer> teamValues = new ArrayList<>();
        for (PgmTeam team : this.teams) {
            if (!team.isObserver()) {
                teamValues.add(team.getPlayerNames().size());
            }
        }
        Collections.sort(teamValues);
        for (PgmTeam team : this.teams) {
            if (team.getPlayerNames().size() == teamValues.get(0) && !team.isObserver()) {
                result = team;
            }
        }
        return result;
    }

    public List<PgmTeam> getTeams() {
        return teams;
    }

}
