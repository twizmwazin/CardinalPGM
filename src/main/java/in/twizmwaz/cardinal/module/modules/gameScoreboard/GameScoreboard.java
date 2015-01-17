package in.twizmwaz.cardinal.module.modules.gameScoreboard;

import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ScoreboardUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

public class GameScoreboard implements Module {

    private TeamModule team;
    private Scoreboard scoreboard;

    public GameScoreboard(TeamModule team) {
        this.team = team;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public TeamModule getTeam() {
        return team;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onObjectiveTouch(ObjectiveTouchEvent event) {
        if (event.updateScoreboard()) {
            updateScoreboard();
        }
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        updateScoreboard();
    }

    public void updateScoreboard() {
        if (scoreboard.getObjective("scoreboard") != null) {
            scoreboard.getObjective("scoreboard").unregister();
        }
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        objective.setDisplayName(ChatColor.GOLD + "Objectives");
        int slot = 1;
        Set<String> used = new HashSet<>();
        if (getSlots() > 15) {

        } else {
            for (TeamModule team : TeamUtils.getTeams()) {
                if (!team.equals(this.team)) {
                    for (GameObjective gameObjective : TeamUtils.getObjectives(team)) {
                        if (gameObjective.showOnScoreboard()) {
                            Team objTeam = scoreboard.registerNewTeam(gameObjective.getScoreboardHandler().getNumber() + "-o");
                            String insert = "";
                            while (used.contains(ScoreboardUtils.getConversion(gameObjective.getScoreboardHandler().getPrefix() + " " + ChatColor.RESET + gameObjective.getName(), insert, true))) {
                                insert += ChatColor.RESET;
                            }
                            String obj = ScoreboardUtils.convertToScoreboard(objTeam, gameObjective.getScoreboardHandler().getPrefix() + " " + ChatColor.RESET + gameObjective.getName(), insert, true);
                            used.add(obj);
                            objective.getScore(obj).setScore(slot);
                            slot ++;
                        }
                    }
                    Team scoreboardTeam = scoreboard.registerNewTeam(team.getId() + "-s");
                    String insert = "";
                    while (used.contains(ScoreboardUtils.getConversion(team.getColor() + team.getName(), insert, true))) {
                        insert += ChatColor.RESET;
                    }
                    String steam = ScoreboardUtils.convertToScoreboard(scoreboardTeam, team.getColor() + team.getName(), insert, true);
                    used.add(steam);
                    objective.getScore(steam).setScore(slot);
                    slot ++;
                    if (!team.isObserver()) {
                        String blank = " ";
                        while (used.contains(blank)) {
                            blank += " ";
                        }
                        used.add(blank);
                        objective.getScore(blank).setScore(slot);
                        slot ++;
                    }
                }
            }
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public int getSlots() {
        int slots = 0;
        for (TeamModule team : TeamUtils.getTeams()) {
            slots += 2;
            for (GameObjective objective : TeamUtils.getObjectives(team)) {
                slots ++;
            }
        }
        slots --;
        return slots;
    }
}
