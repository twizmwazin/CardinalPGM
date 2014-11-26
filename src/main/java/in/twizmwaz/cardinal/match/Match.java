package in.twizmwaz.cardinal.match;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.data.MapInfo;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.match.listeners.ConnectionListener;
import in.twizmwaz.cardinal.match.listeners.MatchListener;
import in.twizmwaz.cardinal.match.listeners.ObserverListener;
import in.twizmwaz.cardinal.match.listeners.TeamListener;
import in.twizmwaz.cardinal.match.util.StartTimer;
import in.twizmwaz.cardinal.teams.PgmTeam;
import in.twizmwaz.cardinal.teams.PgmTeamBuilder;
import in.twizmwaz.cardinal.util.DomUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Match {

    private GameHandler handler;
    private UUID uuid;
    private MatchState state;
    private Document document;
    private MapInfo mapInfo;
    private Scoreboard scoreboard;
    private List<PgmTeam> teams;

    private ConnectionListener connectionListener;
    private MatchListener matchListener;
    private TeamListener teamListener;
    private ObserverListener observerListener;

    public Match(GameHandler handler, UUID id) {
        this.uuid = id;
        this.handler = handler;
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

        mapInfo = new MapInfo(document);
        this.state = MatchState.WAITING;
        this.connectionListener = new ConnectionListener(JavaPlugin.getPlugin(Cardinal.class), this);
        this.matchListener = new MatchListener(JavaPlugin.getPlugin(Cardinal.class), this);
        this.teamListener = new TeamListener(JavaPlugin.getPlugin(Cardinal.class), this);
        this.observerListener = new ObserverListener(JavaPlugin.getPlugin(Cardinal.class), this);


        Bukkit.getServer().getPluginManager().callEvent(new CycleCompleteEvent(this));

    }

    public void unregister() {
        HandlerList.unregisterAll(connectionListener);
        HandlerList.unregisterAll(matchListener);
        HandlerList.unregisterAll(teamListener);
        HandlerList.unregisterAll(observerListener);
    }

    public Match getMatch() {
        return this;
    }

    public UUID getUUID() {
        return uuid;
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

    public UUID getUuid() {
        return uuid;
    }

    public void start(int time) {
        if (state == MatchState.WAITING) {
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(GameHandler.getGameHandler().getPlugin(), new StartTimer(GameHandler.getGameHandler(), time), 0L, 20L);
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
        int most = Integer.MAX_VALUE;
        for (PgmTeam team : this.teams) {
            if (team.getPlayers().size() < most && (!team.getName().equalsIgnoreCase("observers"))) {
                result = team;
            }
        }
        return result;
    }
}
