package in.twizmwaz.cardinal.module.modules.gameScoreboard;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.ScoreboardUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

public class GameScoreboard implements Module {

    private final TeamModule team;
    private Scoreboard scoreboard;

    protected GameScoreboard(final TeamModule team) {
        this.team = team;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        for (TeamModule teams : TeamUtils.getTeams()) {
            Team scoreboardTeam = scoreboard.registerNewTeam(teams.getId());
            scoreboardTeam.setPrefix(teams.getColor() + "");
            scoreboard.registerNewTeam(teams.getId() + "-s");
        }
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            scoreboard.registerNewTeam(objective.getScoreboardHandler().getNumber() + "-o");
        }
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

    public static void add(TeamModule team, Player player) {
        for (GameScoreboard gameScoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(GameScoreboard.class)) {
            gameScoreboard.getScoreboard().getTeam(team.getId()).addPlayer(player);
        }
    }

    public static void remove(TeamModule team, Player player) {
        for (GameScoreboard gameScoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(GameScoreboard.class)) {
            gameScoreboard.getScoreboard().getTeam(team.getId()).removePlayer(player);
        }
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

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        updateScoreboard();
    }

    @EventHandler
    public void onScoreUpdate(ScoreUpdateEvent event) {
        updateScoreboard();
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (event.getNewTeam() == this.team) {
            event.getPlayer().setScoreboard(this.scoreboard);
        }
        for (TeamModule team : TeamUtils.getTeams()) {
            remove(team, event.getPlayer());
        }
        add(event.getNewTeam(), event.getPlayer());
    }

    public void updateScoreboard() {
        if (scoreboard.getObjective("scoreboard") != null) {
            scoreboard.getObjective("scoreboard").unregister();
        }
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        objective.setDisplayName(getDisplayTitle());
        int slot = 1;
        Set<String> used = new HashSet<>();
        if (getSlots() > 15) {
            if (getCompactSlots() <= 15) {
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (team != this.team && !team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                        String compact = "";
                        Team objectiveTeam = null;
                        for (GameObjective gameObjective : TeamUtils.getShownObjectives(team)) {
                            compact += " " + gameObjective.getScoreboardHandler().getPrefix(this.team) + "  ";
                            objectiveTeam = scoreboard.getTeam(gameObjective.getScoreboardHandler().getNumber() + "-o");
                        }
                        if (objectiveTeam != null) {
                            String insertObj = team.getColor() + "";
                            while (used.contains(ScoreboardUtils.getConversion(compact, insertObj))) {
                                insertObj += ChatColor.RESET;
                            }
                            String steam = ScoreboardUtils.convertToScoreboard(objectiveTeam, compact, insertObj);
                            used.add(steam);
                            objective.getScore(steam).setScore(slot);
                            slot++;
                        }
                        Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                        String insert = team.getColor() + "";
                        while (used.contains(ScoreboardUtils.getConversion(team.getName(), insert))) {
                            insert += ChatColor.RESET;
                        }
                        String steam = ScoreboardUtils.convertToScoreboard(scoreboardTeam, team.getName(), insert);
                        used.add(steam);
                        objective.getScore(steam).setScore(slot);
                        slot++;
                        if (slot < getCompactSlots()) {
                            String blank = " ";
                            while (used.contains(blank)) {
                                blank += " ";
                            }
                            used.add(blank);
                            objective.getScore(blank).setScore(slot);
                            slot++;
                        }
                    }
                    if (ScoreModule.matchHasScoring() && !team.isObserver()) {
                        Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                        String insert = team.getColor() + "";
                        while (used.contains(ScoreboardUtils.getConversion(team.getName(), insert))) {
                            insert += ChatColor.RESET;
                        }
                        String steam = ScoreboardUtils.convertToScoreboard(scoreboardTeam, team.getName(), insert);
                        used.add(steam);
                        int score = 0;
                        for (ScoreModule scoreModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                            if (scoreModule.getTeam() == team) {
                                score = scoreModule.getScore();
                            }
                        }
                        if (score == 0) {
                            objective.getScore(steam).setScore(1);
                        }
                        objective.getScore(steam).setScore(score);
                    }
                }
                if (!team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                    String compact = "";
                    Team objectiveTeam = null;
                    for (GameObjective gameObjective : TeamUtils.getShownObjectives(team)) {
                        compact += " " + gameObjective.getScoreboardHandler().getPrefix(this.team) + "  ";
                        objectiveTeam = scoreboard.getTeam(gameObjective.getScoreboardHandler().getNumber() + "-o");
                    }
                    if (objectiveTeam != null) {
                        String insertObj = team.getColor() + "";
                        while (used.contains(ScoreboardUtils.getConversion(compact, insertObj))) {
                            insertObj += ChatColor.RESET;
                        }
                        String steam = ScoreboardUtils.convertToScoreboard(objectiveTeam, compact, insertObj);
                        used.add(steam);
                        objective.getScore(steam).setScore(slot);
                        slot++;
                    }
                    Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                    String insert = team.getColor() + "" + ChatColor.ITALIC;
                    while (used.contains(ScoreboardUtils.getConversion(team.getName(), insert))) {
                        insert += ChatColor.RESET;
                    }
                    String steam = ScoreboardUtils.convertToScoreboard(scoreboardTeam, team.getName(), insert);
                    used.add(steam);
                    objective.getScore(steam).setScore(slot);
                }
                if (ScoreModule.matchHasMax()) {
                    objective.getScore(ChatColor.RED + "---- MAX ----").setScore(ScoreModule.max());
                }
            }
        } else {
            for (TeamModule team : TeamUtils.getTeams()) {
                if (team != this.team && !team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                    for (GameObjective gameObjective : TeamUtils.getShownObjectives(team)) {
                        String insert = "";
                        while (used.contains(ScoreboardUtils.getConversion(" " + gameObjective.getScoreboardHandler().getPrefix(this.team) + " " + ChatColor.RESET + WordUtils.capitalizeFully(gameObjective.getName()), insert, true))) {
                            insert += ChatColor.RESET;
                        }
                        String obj = ScoreboardUtils.convertToScoreboard(scoreboard.getTeam(gameObjective.getScoreboardHandler().getNumber() + "-o"), " " + gameObjective.getScoreboardHandler().getPrefix(this.team) + " " + ChatColor.RESET + WordUtils.capitalizeFully(gameObjective.getName()), insert, true);
                        used.add(obj);
                        objective.getScore(obj).setScore(slot);
                        slot ++;
                    }
                    Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                    String insert = team.getColor() + "";
                    while (used.contains(ScoreboardUtils.getConversion(team.getName(), insert))) {
                        insert += ChatColor.RESET;
                    }
                    String steam = ScoreboardUtils.convertToScoreboard(scoreboardTeam, team.getName(), insert);
                    used.add(steam);
                    objective.getScore(steam).setScore(slot);
                    slot ++;
                    if (slot < getSlots()) {
                        String blank = " ";
                        while (used.contains(blank)) {
                            blank += " ";
                        }
                        used.add(blank);
                        objective.getScore(blank).setScore(slot);
                        slot ++;
                    }
                }
                if (ScoreModule.matchHasScoring() && !team.isObserver()) {
                    Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                    String insert = team.getColor() + "";
                    while (used.contains(ScoreboardUtils.getConversion(team.getName(), insert))) {
                        insert += ChatColor.RESET;
                    }
                    String steam = ScoreboardUtils.convertToScoreboard(scoreboardTeam, team.getName(), insert);
                    used.add(steam);
                    int score = 0;
                    for (ScoreModule scoreModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                        if (scoreModule.getTeam() == team) {
                            score = scoreModule.getScore();
                        }
                    }
                    if (score == 0) {
                        objective.getScore(steam).setScore(1);
                    }
                    objective.getScore(steam).setScore(score);
                }
            }
            if (!team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                for (GameObjective gameObjective : TeamUtils.getShownObjectives(team)) {
                    String insert = "";
                    while (used.contains(ScoreboardUtils.getConversion(" " + gameObjective.getScoreboardHandler().getPrefix(this.team) + " " + ChatColor.RESET + WordUtils.capitalizeFully(gameObjective.getName()), insert, true))) {
                        insert += ChatColor.RESET;
                    }
                    String obj = ScoreboardUtils.convertToScoreboard(scoreboard.getTeam(gameObjective.getScoreboardHandler().getNumber() + "-o"), " " + gameObjective.getScoreboardHandler().getPrefix(this.team) + " " + ChatColor.RESET + WordUtils.capitalizeFully(gameObjective.getName()), insert, true);
                    used.add(obj);
                    objective.getScore(obj).setScore(slot);
                    slot ++;
                }
                Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                String insert = team.getColor() + "" + ChatColor.ITALIC;
                while (used.contains(ScoreboardUtils.getConversion(team.getName(), insert))) {
                    insert += ChatColor.RESET;
                }
                String steam = ScoreboardUtils.convertToScoreboard(scoreboardTeam, team.getName(), insert);
                used.add(steam);
                objective.getScore(steam).setScore(slot);
            }
            if (ScoreModule.matchHasMax()) {
                objective.getScore(ChatColor.RED + "---- MAX ----").setScore(ScoreModule.max());
            }
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public int getSlots() {
        int slots = 0;
        for (TeamModule team : TeamUtils.getTeams()) {
            if (!team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                slots += 2;
                slots += TeamUtils.getShownObjectives(team).size();
            }
        }
        if (ScoreModule.matchHasMax()) slots ++;
        slots --;
        return slots;
    }

    public int getCompactSlots() {
        int slots = 0;
        for (TeamModule team : TeamUtils.getTeams()) {
            if (!team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                slots += 2;
                if (TeamUtils.getShownObjectives(team).size() > 0) slots ++;
            }
        }
        if (ScoreModule.matchHasMax()) slots ++;
        slots --;
        return slots;
    }

    public String getDisplayTitle() {
        boolean hasObjectives = false;
        boolean objectivesAreWools = true;
        for (TeamModule team : TeamUtils.getTeams()) {
            for (GameObjective objective : TeamUtils.getShownObjectives(team)) {
                hasObjectives = true;
                if (!(objective instanceof WoolObjective)) {
                    objectivesAreWools = false;
                }
            }
        }
        if (hasObjectives) {
            if (objectivesAreWools) {
                return "Wools";
            } else {
                return ChatColor.GOLD + "Objectives";
            }
        } else if (ScoreModule.matchHasScoring()) {
            return ChatColor.GOLD + "Scores";
        } else {
            return ChatColor.RED + "" + ChatColor.BOLD + "Invalid";
        }
    }
}
