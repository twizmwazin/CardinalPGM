package in.twizmwaz.cardinal.module.modules.hill;

import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.regions.Region;
import org.bukkit.event.HandlerList;

public class HillObjective implements GameObjective {

    public enum CaptureRule {
        EXCLUSIVE(), MAJORITY(), LEAD();
    }

    private TeamModule team;
    private final String name;
    private final String id;
    private final int captureTime;
    private final int points;
    private final int pointsGrowth;
    private final CaptureRule captureRule;
    private final double timeMultiplier;
    private final boolean showProgress;
    private final boolean neutralState;
    private final boolean incremental;
    private final boolean permanent;
    private final boolean show;
    private final Region capture;
    private final Region progress;
    private final Region captured;

    private double controlTime;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected HillObjective(final TeamModule team, final String name, final String id, final int captureTime, final int points, final int pointsGrowth, final CaptureRule captureRule, final double timeMultiplier, final boolean showProgress, final boolean neutralState, final boolean incremental, final boolean permanent, final boolean show, final Region capture, final Region progress, final Region captured) {
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

    public int getPointsGrowth() {
        return pointsGrowth;
    }

    public int getPoints() {
        return points;
    }

    public int getCaptureTime() {
        return captureTime;
    }

}
