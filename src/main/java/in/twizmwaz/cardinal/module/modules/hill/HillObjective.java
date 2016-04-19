package in.twizmwaz.cardinal.module.modules.hill;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveProximityEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveUncompleteEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.scoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Fireworks;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.BlockVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HillObjective implements TaskedModule, GameObjective {

    private String name, id;
    private int captureTime, points, pointsGrowth;
    private double timeMultiplier;
    private CaptureRule captureRule;
    private boolean showProgress, neutralState, incremental, permanent, show, required;
    private FilterModule visualMaterials, captureFilter, playerFilter;
    private Set<Player> capturingPlayers;
    private TeamModule team, capturingTeam;
    private int controlTime;
    private GameObjectiveScoreboardHandler scoreboardHandler;
    private GameObjectiveProximityHandler proximityHandler;
    private int seconds = 1;
    private RegionModule capture, progress, captured;
    private RegionSave oldProgress, oldCaptured;

    private long lastUpdate = 0;

    protected HillObjective(final TeamModule team, final String name, final String id, final double captureTime,
                            final int points, final int pointsGrowth, final CaptureRule captureRule,
                            final double timeMultiplier, final boolean showProgress, final boolean neutralState,
                            final boolean incremental, final boolean permanent, final boolean show, final boolean required,
                            final RegionModule capture, final RegionModule progress, final RegionModule captured,
                            final FilterModule visualMaterials, final FilterModule captureFilter, final FilterModule playerFilter) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.captureTime = (int) Math.round(captureTime * 1000);
        this.points = points;
        this.pointsGrowth = pointsGrowth;
        this.captureRule = captureRule;
        this.timeMultiplier = timeMultiplier;
        this.showProgress = showProgress;
        this.neutralState = neutralState;
        this.incremental = incremental;
        this.permanent = permanent;
        this.show = show;
        this.required = required;
        this.capture = capture;
        this.progress = progress;
        this.oldProgress = progress == null ? null : new RegionSave(progress);
        this.captured = captured;
        this.oldCaptured = captured == null ? null : new RegionSave(captured);
        this.visualMaterials = visualMaterials;
        this.captureFilter = captureFilter;
        this.playerFilter = playerFilter;

        this.capturingTeam = null;
        this.controlTime = 0;

        scoreboardHandler = new GameObjectiveScoreboardHandler(this);

        this.capturingPlayers = new HashSet<>();

        renderBlocks();
        renderCaptured();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (ObserverModule.testObserverOrDead(event.getPlayer())) return;
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

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
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
        return this.controlTime != 0;
    }

    @Override
    public boolean isComplete() {
        return team != null && controlTime == 0;
    }

    @Override
    public boolean showOnScoreboard() {
        return show;
    }

    @Override
    public boolean isRequired() {
        return required;
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

    @Override
    public GameObjectiveProximityHandler getProximityHandler(TeamModule team) {
        return null;
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

    public boolean showProgress() {
        return showProgress;
    }

    public TeamModule getCapturingTeam() {
        return capturingTeam;
    }

    public int getPercent() {
        return Math.min(100, Math.max(0, (controlTime * 100) / captureTime));
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (lastUpdate == 0) lastUpdate = System.currentTimeMillis();
            int diff = (int)(System.currentTimeMillis() - lastUpdate);
            if (diff < 100) return;
            lastUpdate = System.currentTimeMillis();
            if (pointsGrowth != 0) {
                int matchSeconds = (int)MatchTimer.getTimeInSeconds();
                if (seconds != matchSeconds && matchSeconds % pointsGrowth == 0) {
                    seconds = matchSeconds;
                    points *= 2;
                }
            }

            if (team != null) {
                if (ScoreModule.matchHasScoring()) {
                    for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                        if (score.getTeam() == team) score.addScore((double)points * diff / 1000);
                    }
                }
            }
            tick(diff);
        }
    }

    public void tick(int time) {
        if (capturingPlayers.size() == 0) {
            if (!incremental && controlTime != 0) update(null, time);
            return;
        }
        Map<TeamModule, Integer> playerCounts = new HashMap<>();
        for (TeamModule team : Teams.getTeams()) {
            if (team.isObserver()) continue;
            playerCounts.put(team, 0);
        }

        TeamModule team1 = null, team2 = null;
        int allowedPlayers = 0;
        for(Player player : capturingPlayers) {
            if (!playerFilter.evaluate(player).equals(FilterState.ALLOW)) continue;
            allowedPlayers++;
            TeamModule team = Teams.getTeamByPlayer(player).get();
            int playerCount = playerCounts.get(team) + 1;
            playerCounts.put(team, playerCount);
            if(team != team1) {
                if(team1 == null || playerCount > playerCounts.get(team1)) {
                    team2 = team1;
                    team1 = team;
                } else if(team != team2 && (team2 == null || playerCount > playerCounts.get(team2))) {
                    team2 = team;
                }
            }
        }

        int team1Players = team1 == null ? 0 : playerCounts.get(team1);
        int otherPlayers = allowedPlayers - team1Players;

        if (captureRule.equals(CaptureRule.EXCLUSIVE) && otherPlayers > 0) team1Players = 0;
        if (captureRule.equals(CaptureRule.MAJORITY)) team1Players -= otherPlayers;
        if (captureRule.equals(CaptureRule.LEAD) && team2 != null) team1Players -= playerCounts.get(team2);

        if(team1Players > 0) {
            time += team1Players * timeMultiplier * time;
            update(team1, time);
        } else if (!incremental && controlTime != 0) {
            update(null, time);
        }
    }

    private void update(TeamModule team, int time) {
        TeamModule oldOwningTeam = this.team;
        progress(team, time);
        Bukkit.getServer().getPluginManager().callEvent(new ObjectiveProximityEvent(this, null, 0, 0));
        renderBlocks();
        if(oldOwningTeam != this.team) {
            if (this.team != null) {
                Bukkit.getServer().getPluginManager().callEvent(new ObjectiveCompleteEvent(this, null));
            } else {
                Bukkit.getServer().getPluginManager().callEvent(new ObjectiveUncompleteEvent(this, oldOwningTeam));
            }
            renderCaptured();
        }
    }

    private void renderBlocks() {
        if (progress == null) return;
        byte color1 = capturingTeam != null ? MiscUtil.convertChatColorToDyeColor(capturingTeam.getColor()).getWoolData() : -1;
        byte color2 = team != null ? MiscUtil.convertChatColorToDyeColor(team.getColor()).getWoolData() : -1;
        double x = progress.getCenterBlock().getX() - 0.5;
        double z = progress.getCenterBlock().getZ() - 0.5;
        double percent = Math.toRadians(getPercent() * 3.6);
        for(Block block : progress.getBlocks()) {
            if (!visualMaterials.evaluate(block).equals(FilterState.ALLOW)) continue;
            double dx = block.getX() - x;
            double dz = block.getZ() - z;
            double angle = Math.atan2(dz, dx);
            if(angle < 0) angle += 2 * Math.PI;
            byte color = angle < percent ? color1 : color2;
            if (color == -1) {
                Pair<Material,Byte> oldBlock = oldProgress.getBlockAt(new BlockVector(block.getLocation()));
                if (oldBlock.getLeft().equals(block.getType())) color = oldBlock.getRight();
            }
            if (color != -1) block.setData(color);
        }
    }

    private void renderCaptured() {
        if (captured == null) return;
        for(Block block : captured.getBlocks()) {
            if (!visualMaterials.evaluate(block).equals(FilterState.ALLOW)) continue;
            byte color = team != null ? MiscUtil.convertChatColorToDyeColor(team.getColor()).getWoolData() : -1;
            if (color == -1) {
                Pair<Material,Byte> oldBlock = oldCaptured.getBlockAt(new BlockVector(block.getLocation()));
                if (oldBlock.getLeft().equals(block.getType())) color = oldBlock.getRight();
            }
            if (color != -1) block.setData(color);
        }
    }

    private void progress(TeamModule team, int time) {
        if((permanent && this.team != null) || time <= 0) return;

        if(this.team != null && neutralState) {
            if(team == this.team) {
                if(incremental && controlTime > time) {
                    controlTime = controlTime - time;
                    return;
                }
                controlTime = 0;
                capturingTeam = null;
            } else if(team != null) {
                controlTime += time;
                if(controlTime >= captureTime) {
                    time = controlTime - captureTime;
                    controlTime = 0;
                    this.team = null;
                    progress(team, time);
                }
            } else if(!incremental) {
                controlTime = 0;
            }
        } else if (capturingTeam != null) {
            if(team == capturingTeam) {
                controlTime = controlTime + time;
                if(controlTime >= captureTime) {
                    controlTime = 0;
                    this.team = capturingTeam;
                    capturingTeam = null;
                }
            } else if(team != null) {
                if(incremental && controlTime > time) {
                    controlTime = controlTime - time;
                    return;
                } else if (incremental) time -= controlTime;
                controlTime = 0;
                capturingTeam = null;
                progress(team, time);
            } else if(!incremental) {
                controlTime = 0;
                capturingTeam = null;
            }
        } else if(team != null && team != this.team && captureFilter.evaluate(team).equals(FilterState.ALLOW)) {
            capturingTeam = team;
            progress(team, time);
        }
    }

    @EventHandler
    public void onHillCapture(ObjectiveCompleteEvent event) {
        if (event.getObjective().equals(this) && this.team != null && showOnScoreboard()) {
            Fireworks.spawnFireworks(capture.getCenterBlock().getAlignedVector(), (capture.getMax().minus(capture.getMin()).length()) * 0.55 + 1, 6, MiscUtil.convertChatColorToColor(team.getColor()), 1);
        }
    }

}
