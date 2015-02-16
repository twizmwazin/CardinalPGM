package in.twizmwaz.cardinal.module.modules.hill;

import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.event.HandlerList;

public class HillObjective implements GameObjective {

    private TeamModule team;
    private final String name;
    private final String id;
    private final int captureTime;
    private final int points;
    private final double pointsGrowth;
    private final CaptureRule captureRule;
    private final double timeMultiplier;
    private final boolean showProgress;
    private final boolean neutralState;
    private final boolean incremental;
    private final boolean permanent;
    private final boolean show;
    private final RegionModule capture;
    private final RegionModule progress;
    private final RegionModule captured;

    private TeamModule capturingTeam;
    private double controlTime;

    private GameObjectiveScoreboardHandler scoreboardHandler;

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
}
