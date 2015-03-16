package in.twizmwaz.cardinal.module.modules.hill;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

public class HillObjective implements TaskedModule, GameObjective {

    private TeamModule team, capturingTeam;
    private final String name, id;
    private final int captureTime, points;
    private final double pointsGrowth, timeMultiplier;
    private final CaptureRule captureRule;
    private final boolean showProgress ,neutralState, incremental, permanent, show;
    private final RegionModule capture, progress, captured;
    private double controlTime;

    private GameObjectiveScoreboardHandler scoreboardHandler;
    private final Set<Player> capturingPlayers;

    private int seconds = 1;
    private int tempPoints;

    protected HillObjective(final TeamModule team, final String name, final String id, final int captureTime, final int points, final double pointsGrowth, final CaptureRule captureRule, final double timeMultiplier, final boolean showProgress, final boolean neutralState, final boolean incremental, final boolean permanent, final boolean show, final RegionModule capture, final RegionModule progress, final RegionModule captured) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.captureTime = captureTime;
        this.points = points;
        this.pointsGrowth = pointsGrowth;
        this.captureRule = captureRule;
        this.timeMultiplier = timeMultiplier;
        this.showProgress = showProgress;
        this.neutralState = neutralState;
        this.incremental = incremental;
        this.permanent = permanent;
        this.show = show;
        this.capture = capture;
        this.progress = progress;
        this.captured = captured;

        this.capturingTeam = null;
        this.controlTime = 0;

        scoreboardHandler = new GameObjectiveScoreboardHandler(this);
        this.capturingPlayers = new HashSet<>();
        this.tempPoints = points;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (capture.contains(event.getTo()) && !capturingPlayers.contains(event.getPlayer()))
            capturingPlayers.add(event.getPlayer());
        if (!capture.contains(event.getTo()) && capturingPlayers.contains(event.getPlayer()))
            capturingPlayers.remove(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (capturingPlayers.contains(event.getEntity())) capturingPlayers.remove(event.getEntity());
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (capturingPlayers.contains(event.getPlayer())) capturingPlayers.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerKickEvent event) {
        if (capturingPlayers.contains(event.getPlayer())) capturingPlayers.remove(event.getPlayer());
    }

    @Override
    public TeamModule getTeam() {
        return this.team;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isTouched() {
        return this.controlTime > 0 && (controlTime / captureTime) < 1;
    }

    @Override
    public boolean isComplete() {
        return (controlTime / captureTime) >= 1;
    }

    @Override
    public boolean showOnScoreboard() {
        return show;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
        capturingPlayers.clear();
    }

    @Override
    public GameObjectiveScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    public double getPointsGrowth() {
        return pointsGrowth;
    }

    public int getPoints() {
        return points;
    }

    public int getCaptureTime() {
        return captureTime;
    }

    public boolean showProgress() {
        return showProgress;
    }

    public TeamModule getCapturingTeam() {
        return capturingTeam;
    }

    public int getPercent() {
        return (int) ((controlTime / captureTime) * 100 > 100 ? 100 : (controlTime / captureTime) * 100);
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (seconds <= MatchTimer.getTimeInSeconds()) {
                seconds ++;
                if (team != null) {
                    if (seconds % pointsGrowth == 0) {
                        tempPoints = (int) Math.pow(tempPoints, 2);
                    }
                    if (ScoreModule.matchHasScoring()) {
                        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                            if (score.getTeam() == team) {
                                score.setScore(score.getScore() + tempPoints);
                                Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(score));
                            }
                        }

                    }
                }
            }
        }
    }
}
