package in.twizmwaz.cardinal.module.modules.scoreboard;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.event.TeamNameChangeEvent;
import in.twizmwaz.cardinal.event.TimeLimitChangeEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveProximityEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.Scoreboards;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreboardModule implements Module {

    private TeamModule team, prioritized;
    private Scoreboard scoreboard;

    private Objective objective;
    private int currentScore, currentHillScore, minBlitzScore = -1, minTdmScore = -1;
    private List<String> used;

    public ScoreboardModule(final TeamModule team) {
        this.team = team;
        this.prioritized = team;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        for (TeamModule teams : Teams.getTeams()) {
            Team prefixTeam = scoreboard.registerNewTeam(teams.getId());
            prefixTeam.setPrefix(teams.getColor() + "");
            if (!teams.isObserver()) {
                scoreboard.registerNewTeam(teams.getId() + "-t");
            }
        }
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            scoreboard.registerNewTeam(objective.getScoreboardHandler().getNumber() + "-o");
        }
        if (ScoreModule.matchHasScoring()) {
            for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                scoreboard.registerNewTeam(score.getTeam().getId() + "-s");
            }
        }
        if (Blitz.matchIsBlitz()) {
            for (TeamModule teams : Teams.getTeams()) {
                if (!teams.isObserver()) {
                    scoreboard.registerNewTeam(teams.getId() + "-b");
                }
            }
        }
    }

    public static void add(TeamModule team, Player player) {
        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
            scoreboard.getScoreboard().getTeam(team.getId()).addPlayer(player);
        }
    }

    public static void remove(TeamModule team, Player player) {
        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
            scoreboard.getScoreboard().getTeam(team.getId()).removePlayer(player);
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (!event.isCancelled()) {
            if (event.getNewTeam().orNull() == this.team) {
                event.getPlayer().setScoreboard(this.scoreboard);
            }
            for (TeamModule team : Teams.getTeams()) {
                remove(team, event.getPlayer());
            }
            if (event.getNewTeam().isPresent()) {
                add(event.getNewTeam().get(), event.getPlayer());
            }
            if (Blitz.matchIsBlitz()) {
                updateTeamBlitz();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event){
        updateTeamBlitz();
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        update();
    }

    @EventHandler
    public void onObjectiveProximity(ObjectiveProximityEvent event) {
        if (!this.team.isObserver()) {
            if ((event.getObjective() instanceof WoolObjective && !(event.getObjective().getTeam() == this.team)) ||
                    ((event.getObjective() instanceof CoreObjective || event.getObjective() instanceof DestroyableObjective) && (event.getObjective().getTeam() == this.team))) {
                return;
            }
        }
        updateObjectivePrefix(event.getObjective());
        updateObs();
    }

    @EventHandler
    public void onTimeLimitChange(TimeLimitChangeEvent event) {
        update();
    }

    @EventHandler
    public void onObjectiveTouch(ObjectiveTouchEvent event) {
        updateObjectivePrefix(event.getObjective());
        updateObs();
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        updateObjectivePrefix(event.getObjective());
        updateObs();
    }

    @EventHandler
    public void onTeamNameChange(TeamNameChangeEvent event) {
        updateTeamTitle(event.getTeam());
        if (Blitz.matchIsBlitz()) {
            updateTeamBlitz();
        }
        if (ScoreModule.matchHasScoring()) {
            updateTeamScore();
        }
    }

    @EventHandler
    public void onScoreUpdate(ScoreUpdateEvent event) {
        updateTeamScore();
    }

    public void updateObs() {
        if (this.team.isObserver()) {
            TeamModule winner = TimeLimit.getMatchWinner();
            if (winner != prioritized) {
                prioritized = winner;
                update();
            }
        }
    }
    public void update() {

        objective = scoreboard.getObjective("scoreboard") == null ? scoreboard.registerNewObjective("scoreboard", "dummy") : scoreboard.getObjective("scoreboard");
        objective.setDisplayName(getDisplayTitle());
        currentScore = 0;
        currentHillScore = (getSpecificObjective() != null && getSpecificObjective().equals(HillObjective.class)) && !ScoreModule.matchHasScoring() ? 0 : -1;
        used = new ArrayList<>();

        for (TeamModule team : Teams.getTeams()) {
            if (!team.isObserver() && team != prioritized && Teams.getShownObjectives(team).size() > 0) {
                if (currentScore != 0) {
                    setBlankSlot(currentScore);
                    currentScore++;
                }
                if (getSlots() < 16) {
                    for (GameObjective obj : Teams.getShownObjectives(team)) {
                        renderObjective(obj);
                    }
                } else {
                    renderCompactObjectives(Teams.getShownObjectives(team));
                }
                renderTeamTitle(team);
            }
        }
        if (prioritized != null && !prioritized.isObserver() && Teams.getShownObjectives(prioritized).size() > 0) {
            if (currentScore != 0) {
                setBlankSlot(currentScore);
                currentScore++;
            }
            if (getSlots() < 16) {
                for (GameObjective obj : Teams.getShownObjectives(prioritized)) {
                    renderObjective(obj);
                }
            } else {
                renderCompactObjectives(Teams.getShownObjectives(prioritized));
            }
            renderTeamTitle(prioritized);
        }
        if (ScoreModule.matchHasScoring()) {
            if (currentScore != 0) {
                setBlankSlot(currentScore);
                currentScore++;
            }
            renderTeamScore();
        }
        if (Blitz.matchIsBlitz()) {
            if (currentScore != 0) {
                setBlankSlot(currentScore);
                currentScore++;
            }
            renderTeamBlitz();
        }
        if (Scoreboards.getHills().size() > 0) {
            if (!((getSpecificObjective() != null && getSpecificObjective().equals(HillObjective.class)) && !ScoreModule.matchHasScoring())) {
                setBlankSlot(currentHillScore);
                currentHillScore--;
            }
            if (getSlots() < 16) {
                for (HillObjective obj : Scoreboards.getHills()) {
                    renderObjective(obj);
                }
            } else {
                ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
                for (HillObjective obj : Scoreboards.getHills()) {
                    objectives.add(obj);
                }
                renderCompactObjectives(objectives);
            }
        }

        if (objective.getDisplaySlot() == null || !objective.getDisplaySlot().equals(DisplaySlot.SIDEBAR)) {
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }

    public void updateObjectivePrefix(GameObjective objective) {
        if (getSlots() < 16) {
            Team team = scoreboard.getTeam(objective.getScoreboardHandler().getNumber() + "-o");
            String prefix = objective.getScoreboardHandler().getPrefix(this.team);
            team.setPrefix(prefix);
        } else {
            update();
        }
    }

    public void updateTeamTitle(TeamModule teamModule) {
        Team team = scoreboard.getTeam(teamModule.getId() + "-t");
        team.setPrefix(teamModule.getColor() + Strings.trimTo(teamModule.getName(), 0, 14));
        team.setSuffix(Strings.trimTo(teamModule.getName(), 14, 30));
    }
    public void updateTeamBlitz() {
        if (minBlitzScore == -1) {
            update();
        } else {
            renderTeamBlitz();
        }
    }

    public void updateTeamScore() {
        if (minTdmScore == -1) {
            update();
        } else {
            renderTeamScore();
        }
    }

    public void setBlankSlot(int score) {
        String blank = " ";
        while (used.contains(blank)) {
            blank += " ";
        }
        setScore(objective, blank, score);
        used.add(blank);
    }

    public void setScore(Objective objective, String string, int score) {
        if (score == 0) {
            objective.getScore(string).setScore(-1);
        }
        objective.getScore(string).setScore(score);
    }

    public int getSlots() {
        int slots = 0;
        for (TeamModule team : Teams.getTeams()) {
            if (!team.isObserver() && Teams.getShownObjectives(team).size() > 0) {
                if (slots != 0) {
                    slots++;
                }
                slots += Teams.getShownObjectives(team).size() + 1;
            }
        }
        if (ScoreModule.matchHasScoring()) {
            if (slots != 0) {
                slots++;
            }
            slots += (Teams.getTeams().size() - 1);
        }
        if (Blitz.matchIsBlitz())
            if (slots != 0) {
                slots++;
            }
            slots += (Teams.getTeams().size() - 1);
        if (Scoreboards.getHills().size() > 0) {
            if (slots != 0) {
                slots++;
            }
            slots += Scoreboards.getHills().size();
        }
        return slots;
    }

    public String getDisplayTitle() {
        String displayTitle = "";
        boolean hasObjectives = false;
        for (GameObjective obj : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (obj.showOnScoreboard()) {
                hasObjectives = true;
            }
        }
        Class objective = getSpecificObjective();
        if (hasObjectives) {
            if (objective != null) {
                if (objective.equals(WoolObjective.class)) {
                    displayTitle = "Wools";
                } else if (objective.equals(CoreObjective.class)) {
                    displayTitle = "Cores";
                } else if (objective.equals(DestroyableObjective.class)) {
                    displayTitle = "Monuments";
                } else if (objective.equals(HillObjective.class)) {
                    displayTitle = "Hills";
                }
            } else {
                displayTitle = "Objectives";
            }
        }
        if (ScoreModule.matchHasScoring()) {
            displayTitle = displayTitle.equals("") || displayTitle.equals("Hills") ? "Scores" : "Objectives";
        }
        if (Blitz.matchIsBlitz()) {
            String blitzTitle = GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class).getTitle();
            displayTitle = (blitzTitle.equals("Blitz") || blitzTitle.equals("Blitz: Rage")) ? (displayTitle.equals("") ? blitzTitle : "Players Remaining") : blitzTitle;
        }
        if (displayTitle.equals("")) {
            return ChatColor.RED + "" + ChatColor.BOLD + "Invalid";
        }
        return ChatColor.AQUA + displayTitle;
    }

    public Class getSpecificObjective() {
        Class objective = null;
        boolean checkHills = true;
        for (TeamModule team : Teams.getTeams()) {
            for (GameObjective obj : Teams.getShownObjectives(team)) {
                if (objective == null) {
                    objective = obj.getClass();
                } else if (objective != obj.getClass()) {
                    objective = null;
                    checkHills = false;
                    break;
                }
            }
        }
        if (checkHills) {
            for (GameObjective obj : Scoreboards.getHills()) {
                if (objective == null) {
                    objective = obj.getClass();
                } else if (objective != obj.getClass()) {
                    objective = null;
                }
            }
        }
        return objective;
    }

    public void renderObjective(GameObjective objective) {
        int score = objective instanceof HillObjective ? currentHillScore : currentScore;
        Team team = scoreboard.getTeam(objective.getScoreboardHandler().getNumber() + "-o");
        String prefix = objective.getScoreboardHandler().getPrefix(this.team);
        team.setPrefix(prefix);
        if (team.getEntries().size() > 0) {
            setScore(this.objective, new ArrayList<>(team.getEntries()).get(0), score);
        } else {
            String raw = ChatColor.RESET + " " + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " "));
            while (used.contains(raw)) {
                raw = ChatColor.RESET + raw;
            }
            team.addEntry(raw);
            setScore(this.objective, raw, score);
            used.add(raw);
        }
        if (objective instanceof HillObjective) {
            if (currentHillScore < 0) {
                currentHillScore--;
            } else {
                currentHillScore++;
            }
        } else {
            currentScore++;
        }
    }

    public void renderCompactObjectives(ModuleCollection<GameObjective> objectives) {
        boolean hills = true;
        for (GameObjective objective : objectives) {
            if (!(objective instanceof HillObjective)) {
                hills = false;
            }
        }
        int score = hills ? currentHillScore : currentScore;
        Team team = scoreboard.getTeam(objectives.get(0).getScoreboardHandler().getNumber() + "-o");
        if (team != null) {
            String compact = "";
            for (GameObjective obj : objectives) {
                compact += obj.getScoreboardHandler().getCompactPrefix(this.team) + " ";
            }
            while (compact.length() > 32) {
                compact = Strings.removeLastWord(compact);
            }
            if (compact.length() < 16){
                team.setPrefix(Strings.trimTo(compact, 0, 16));
                team.setSuffix("r");
            } else if (compact.charAt(15) == '\u00A7') {
                team.setPrefix(Strings.trimTo(compact, 0, 15));
                team.setSuffix(Strings.trimTo(compact, 15, 31));
            } else {
                team.setPrefix(Strings.trimTo(compact, 0, 16));
                team.setSuffix(Strings.getCurrentChatColor(compact, 16).charAt(1) + Strings.trimTo(compact, 16, 31));
            }
            if (team.getEntries().size() > 0) {
                setScore(objective, new ArrayList<>(team.getEntries()).get(0), currentScore);
            } else {
                String name = "\u00A7";
                while (used.contains(name)) {
                    name = ChatColor.RESET + name;
                }
                team.addEntry(name);
                setScore(objective, name, score);
                used.add(name);
            }
            if (hills) {
                if (currentHillScore < 0) {
                    currentHillScore--;
                } else {
                    currentHillScore++;
                }
            } else {
                currentScore++;
            }
        }
    }

    public void renderTeamTitle(TeamModule teamModule) {
        Team team = scoreboard.getTeam(teamModule.getId() + "-t");
        team.setPrefix(teamModule.getColor() + Strings.trimTo(teamModule.getName(), 0, 14));
        team.setSuffix(Strings.trimTo(teamModule.getName(), 14, 30));
        if (team.getEntries().size() > 0) {
            setScore(objective, new ArrayList<>(team.getEntries()).get(0), currentScore);
        } else {
            String name = teamModule.getColor() + "";
            while (used.contains(name)) {
                name = teamModule.getColor() + name;
            }
            team.addEntry(name);
            setScore(objective, name, currentScore);
            used.add(name);
        }
        currentScore++;
    }

    public void renderTeamScore() {
        if (minTdmScore == -1){
            minTdmScore = currentScore;
        }
        List<String> fullNames = new ArrayList<>();
        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)){
            if (score.getMax() != 0){
                fullNames.add(score.getScore() + "" + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + score.getMax() + " " + score.getTeam().getCompleteName());
            } else {
                fullNames.add(score.getScore() + " " + score.getTeam().getCompleteName());
            }
        }
        java.util.Collections.sort(fullNames, new Comparator<String>(){
            public int compare(String str1, String str2){
                int int1 = Integer.parseInt((str1.contains("/") ? str1.split("\u00a7",2) : str1.split(" ",2))[0]);
                int int2 = Integer.parseInt((str2.contains("/") ? str2.split("\u00a7",2) : str2.split(" ",2))[0]);
                return int1 - int2;
            }
        });
        for (int i = 0; i < fullNames.size(); i++) {
            String teamCompleteName = fullNames.get(i).split(" ",2)[1];
            Team team = scoreboard.getTeam(Teams.getTeamByName(teamCompleteName.substring(2)).get().getId() + "-s");
            team.setPrefix(Strings.trimTo(fullNames.get(i), 0, 16));
            team.setSuffix(Strings.trimTo(fullNames.get(i), 16, 32));
            if (team.getEntries().size() > 0) {
                setScore(objective, new ArrayList<>(team.getEntries()).get(0), minTdmScore + i);
            } else {
                String color = teamCompleteName.substring(0, 2);
                String name = color + "";
                while (used.contains(name)) {
                    name = color + name;
                }
                team.addEntry(name);
                setScore(objective, name, minTdmScore + i);
                used.add(name);
            }
            currentScore++;
        }
    }

    public void renderTeamBlitz() {
        if (minBlitzScore == -1){
            minBlitzScore = currentScore;
        }
        List<String> fullNames = new ArrayList<>();
        for (TeamModule team : Teams.getTeams()) {
            if (!team.isObserver()) {
                fullNames.add(team.size() + " " + team.getCompleteName());
            }
        }
        java.util.Collections.sort(fullNames);
        for (int i = 0; i < fullNames.size(); i++) {
            String teamCompleteName = fullNames.get(i).split(" ", 2)[1];
            Team team = scoreboard.getTeam(Teams.getTeamByName(teamCompleteName.substring(2)).get().getId() + "-b");
            team.setPrefix(Strings.trimTo(fullNames.get(i), 0, 16));
            team.setSuffix(Strings.trimTo(fullNames.get(i), 16, 32));
            if (team.getEntries().size() > 0) {
                setScore(objective, new ArrayList<>(team.getEntries()).get(0), minBlitzScore + i);
            } else {
                String color = teamCompleteName.substring(0, 2);
                String name = color + "";
                while (used.contains(name)) {
                    name = color + name;
                }
                team.addEntry(name);
                setScore(objective, name, minBlitzScore + i);
                used.add(name);
            }
            currentScore++;
        }
    }
}
