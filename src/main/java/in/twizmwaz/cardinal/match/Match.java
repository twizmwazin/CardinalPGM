package in.twizmwaz.cardinal.match;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.util.StartTimer;
import in.twizmwaz.cardinal.data.MapInfo;
import in.twizmwaz.cardinal.module.ModuleContainer;
import in.twizmwaz.cardinal.player.PgmPlayer;
import in.twizmwaz.cardinal.teams.PgmTeam;
import in.twizmwaz.cardinal.teams.PgmTeamBuilder;
import in.twizmwaz.cardinal.util.DomUtil;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Match {

    private GameHandler handler = GameHandler.getGameHandler();
    private UUID uuid;
    private MatchState state;
    private ModuleContainer modules;
    private Document document;
    private MapInfo mapInfo;
    private Scoreboard scoreboard;
    private List<PgmTeam> teams;

    public Match() {
        this.uuid = handler.getMatchUUID();
        this.state = MatchState.WAITING;
        try {
            this.document = DomUtil.parse(new File("matches/" + this.uuid.toString() + "/map.xml"));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.modules = new ModuleContainer(this);
        this.mapInfo = new MapInfo(document);

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        PgmTeamBuilder teamBuilder = new PgmTeamBuilder(this, scoreboard);
        teamBuilder.run();
        teams = teamBuilder.getTeams();


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

    public ModuleContainer getModules() {
        return modules;
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

    public void end()  {
        state = MatchState.ENDED;
    }

    public PgmTeam getTeam(PgmPlayer player) {
        for (PgmTeam team : teams) {
            if (team.hasPlayer(player)) return team;
        }
        return null;
    }
}
