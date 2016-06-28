package in.twizmwaz.cardinal.module.modules.scoreboard;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
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
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.team.PlayerModule;
import in.twizmwaz.cardinal.module.modules.team.PlayerModuleManager;
import in.twizmwaz.cardinal.module.modules.rage.Rage;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.Scoreboards;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreboardModule implements Module {

    private static String title;

    private TeamModule team, prioritized;
    private Scoreboard scoreboard;
    private List<TeamModule> sortedTeams;

    private Objective objective;
    private int currentScore, minBlitzScore = -1, minTdmScore = -1;
    private List<String> used = Lists.newArrayList();
    private boolean created = false;

    public ScoreboardModule(final TeamModule team) {
        this.team = team;
        this.prioritized = team.isObserver() ? null : team;
        this.sortedTeams = Teams.getTeams();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        for (TeamModule teams : Teams.getTeams()) {
            addTeam(teams);
        }
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            getOrRegister(objective.getScoreboardHandler().getNumber() + "-o");
        }
    }

    public static void setTitle(String title) {
        ScoreboardModule.title = title;
    }

    public void addTeam(TeamModule team) {
        Team prefixTeam = getOrRegister(team.getId());
        prefixTeam.setPrefix(team.getColor() + "");
        prefixTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        if (!team.isObserver()) {
            getOrRegister(team.getId() + "-t");
            if (ScoreModule.matchHasScoring()) {
                getOrRegister(team.getId() + "-s");
            }
            if (Blitz.matchIsBlitz() && !(team instanceof PlayerModule && ScoreModule.matchHasScoring())) {
                getOrRegister(team.getId() + "-b");
            }
        }
    }

    public void removeTeam(TeamModule team) {
        remove(getOrRegister(team.getId()));
        if (!team.isObserver()) {
            remove(getOrRegister(team.getId() + "-t"));
            if (ScoreModule.matchHasScoring()) {
                remove(getOrRegister(team.getId() + "-s"));
            }
            if (Blitz.matchIsBlitz() && !(team instanceof PlayerModule && ScoreModule.matchHasScoring())) {
                remove(getOrRegister(team.getId() + "-b"));
            }
        }
    }

    public static void addPlayerModule(PlayerModule player) {
        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
            scoreboard.addTeam(player);
            scoreboard.update();
        }
    }

    public static void removePlayerModule(PlayerModule player) {
        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
            scoreboard.removeTeam(player);
            scoreboard.update();
        }
    }

    public void add(TeamModule team, Player player) {
            scoreboard.getTeam(team.getId()).addEntry(player.getName());
    }

    public TeamModule getTeam() {
        return team;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private Team getOrRegister(String id) {
        return scoreboard.getTeam(id) != null ? scoreboard.getTeam(id) : scoreboard.registerNewTeam(id);
    }

    private void remove(Team team) {
        if (team.getEntries().size() > 0) {
            String entry = new ArrayList<>(team.getEntries()).get(0);
            scoreboard.resetScores(entry);
            team.removeEntry(entry);
            used.remove(entry);
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMatchStartEvent(MatchStartEvent event) {
        for (TeamModule team : Teams.getTeamsAndPlayers()) {
            Team scoreboardTeam = scoreboard.getTeam(team.getId());
            if (!team.isObserver()) scoreboardTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }

    @EventHandler
    public void onMatchEndEvent(MatchEndEvent event) {
        for (TeamModule team : Teams.getTeamsAndPlayers()) {
            Team scoreboardTeam = scoreboard.getTeam(team.getId());
            if (!team.isObserver()) scoreboardTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (!event.isCancelled()) {
            if (!(event.getNewTeam().get() instanceof PlayerModuleManager)) {
                if (event.getNewTeam().orNull() == this.team || (event.getNewTeam().get() instanceof PlayerModule && this.team instanceof PlayerModuleManager)) {
                    event.getPlayer().setScoreboard(this.scoreboard);
                }
                if (event.getNewTeam().isPresent()) {
                    scoreboard.getTeam(event.getNewTeam().get().getId()).addEntry(event.getPlayer().getName());
                }
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
                    ((event.getObjective() instanceof CoreObjective || event.getObjective() instanceof DestroyableObjective || event.getObjective() instanceof FlagObjective) && (event.getObjective().getTeam() == this.team))) {
                return;
            }
        }
        updateObjectivePrefix(event.getObjective());
        updateTeamOrder();
    }

    @EventHandler
    public void onTimeLimitChange(TimeLimitChangeEvent event) {
        update();
    }

    @EventHandler
    public void onObjectiveTouch(ObjectiveTouchEvent event) {
        updateObjectivePrefix(event.getObjective());
        updateTeamOrder();
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        updateObjectivePrefix(event.getObjective());
        updateTeamOrder();
    }

    @EventHandler
    public void onTeamNameChange(TeamNameChangeEvent event) {
        if (event.getTeam().isObserver()) return;
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

    public void updateTeamOrder() {
        List<TeamModule> newList = TimeLimit.getSortedTeams();
        if (sortedTeams != newList){
            sortedTeams = newList;
            update();
        }
    }

    public void update() {
        if (!created) {
            objective = scoreboard.getObjective("scoreboard") == null ? scoreboard.registerNewObjective("scoreboard", "dummy") : scoreboard.getObjective("scoreboard");
            objective.setDisplayName(getDisplayTitle());
        }
        currentScore = 0;

        renderObjectives(Scoreboards.getHills());
        renderObjectives(Teams.getShownSharedObjectives());
        for (TeamModule team : sortedTeams) {
            if (!team.isObserver() && team != prioritized && Teams.getShownObjectives(team).size() > 0) {
                if (getSlots() < 16) {
                    renderObjectives(Teams.getShownObjectives(team));
                } else {
                    createBlankSlot();
                    renderCompactObjectives(Teams.getShownObjectives(team));
                }
                renderTeamTitle(team);
            }
        }
        if (prioritized != null && Teams.getShownObjectives(prioritized).size() > 0) {
            if (getSlots() < 16) {
                renderObjectives(Teams.getShownObjectives(prioritized));
            } else {
                createBlankSlot();
                renderCompactObjectives(Teams.getShownObjectives(prioritized));
            }
            renderTeamTitle(prioritized);
        }
        if (ScoreModule.matchHasScoring()) {
            createBlankSlot();
            renderTeamScore();
        }
        if (Blitz.matchIsBlitz() && !(Teams.isFFA() && ScoreModule.matchHasScoring())) {
            createBlankSlot();
            renderTeamBlitz();
        }

        if (objective.getDisplaySlot() == null || !objective.getDisplaySlot().equals(DisplaySlot.SIDEBAR)) {
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        if (currentScore == 0) {
            setScore(objective, "", 0);
        } else if (scoreboard.getEntries().contains("")) {
            scoreboard.resetScores("");
        }
        created = true;
    }

    public void updateObjectivePrefix(GameObjective objective) {
        if (!objective.showOnScoreboard()) return;
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
        setTeamDisplay(scoreboard.getTeam(teamModule.getId() + "-t"), teamModule.getCompleteName(), teamModule.getColor());
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

    private void createBlankSlot() {
        if (currentScore != 0) {
            if (!created) setBlankSlot(currentScore);
            currentScore++;
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
        if (Scoreboards.getHills().size() > 0) {
            slots += Scoreboards.getHills().size();
        }
        if (Teams.getShownSharedObjectives().size() > 0) {
            if (slots != 0) slots++;
            slots += Teams.getShownSharedObjectives().size();
        }
        for (TeamModule team : Teams.getTeams()) {
            if (!team.isObserver() && Teams.getShownObjectives(team).size() > 0) {
                if (slots != 0) slots++;
                slots += Teams.getShownObjectives(team).size() + 1;
            }
        }
        if (ScoreModule.matchHasScoring()) {
            if (slots != 0) slots++;
            slots += (Teams.getTeamsAndPlayers().size() - 1);
        }
        if (Blitz.matchIsBlitz()) {
            if (slots != 0) slots++;
            slots += (Teams.getTeamsAndPlayers().size() - 1);
        }
        return slots;
    }

    public String getDisplayTitle() {
        if (title != null) return ChatColor.AQUA + title;

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
                } else if (objective.equals(FlagObjective.class)) {
                    displayTitle = "Flags";
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
            displayTitle = displayTitle.equals("") || displayTitle.equals("Flags")|| displayTitle.equals("Hills") ? "Scores" : "Objectives";
        }
        if (Blitz.matchIsBlitz()) {
            displayTitle = displayTitle.equals("") ? (GameHandler.getGameHandler().getMatch().getModules().getModule(Rage.class) != null ? "Blitz: Rage" : "Blitz") : "Players Remaining";
        }
        if (displayTitle.equals("")) {
            return ChatColor.RED + "" + ChatColor.BOLD + "Invalid";
        }
        return ChatColor.AQUA + displayTitle;
    }

    public Class getSpecificObjective() {
        Class objective = null;
        for (GameObjective obj : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (obj.showOnScoreboard()) {
                if (objective == null) {
                    objective = obj.getClass();
                } else if (objective != obj.getClass()) {
                    objective = null;
                    break;
                }
            }
        }
        return objective;
    }

    private void renderObjectives(List<GameObjective> objectives) {
        Collections.reverse(objectives);
        if (currentScore != 0 && objectives.size() > 0) createBlankSlot();
        for (GameObjective obj : objectives) renderObjective(obj);
    }

    public void renderObjective(GameObjective objective) {
        if (!objective.showOnScoreboard()) return;
        int score = currentScore;
        Team team = scoreboard.getTeam(objective.getScoreboardHandler().getNumber() + "-o");
        String prefix = objective.getScoreboardHandler().getPrefix(this.team);
        team.setPrefix(prefix);
        if (team.getEntries().size() > 0) {
            setScore(this.objective, new ArrayList<>(team.getEntries()).get(0), score);
        } else {
            String raw = (objective instanceof HillObjective ? "" : ChatColor.RESET) + " " + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " "));
            while (used.contains(raw)) {
                raw =  raw + ChatColor.RESET;
            }
            team.addEntry(raw);
            setScore(this.objective, raw, score);
            used.add(raw);
        }
        currentScore++;
    }

    public void renderCompactObjectives(ModuleCollection<GameObjective> objectives) {
        int score = currentScore;
        Team team = scoreboard.getTeam(objectives.get(0).getScoreboardHandler().getNumber() + "-o");
        if (team != null) {
            String compact = "";
            for (GameObjective obj : objectives) {
                compact += obj.getScoreboardHandler().getCompactPrefix(this.team) + " ";
            }
            while (compact.length() > 32) {
                compact = Strings.removeLastWord(compact);
            }
            setTeamDisplay(team, compact, ChatColor.RESET, true);
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
            currentScore++;
        }
    }

    public void renderTeamTitle(TeamModule teamModule) {
        Team team = scoreboard.getTeam(teamModule.getId() + "-t");
        setTeamDisplay(team, teamModule.getCompleteName(), teamModule.getColor());
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
        ModuleCollection<ScoreModule> scoreModules = GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class);
        Collections.sort(scoreModules, new Comparator<ScoreModule>() {
            @Override
            public int compare(ScoreModule score1, ScoreModule score2) {
                return score1.getScore() - score2.getScore();
            }
        });
        int i = 0;
        for (ScoreModule score : scoreModules) {
            Team team = scoreboard.getTeam(score.getTeam().getId() + "-s");
            String display = score.getScore() + (score.getMax() != 0 ? ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + score.getMax() : "") + " " + score.getTeam().getCompleteName();
            setTeamDisplay(team, display, score.getTeam().getColor());
            if (team.getEntries().size() > 0) {
                if (objective.getScore(new ArrayList<>(team.getEntries()).get(0)).getScore() != minTdmScore + i)
                    setScore(objective, new ArrayList<>(team.getEntries()).get(0), minTdmScore + i);
            } else {
                String name = score.getTeam().getColor() + "";
                while (used.contains(name)) {
                    name = score.getTeam().getColor() + name;
                }
                team.addEntry(name);
                setScore(objective, name, minTdmScore + i);
                used.add(name);
            }
            i++;
            currentScore++;
        }
    }

    public void renderTeamBlitz() {
        if (minBlitzScore == -1){
            minBlitzScore = currentScore;
        }
        ModuleCollection<TeamModule> teams = Teams.getTeamsAndPlayers();
        Collections.sort(teams, new Comparator<TeamModule>() {
            @Override
            public int compare(TeamModule team1, TeamModule team2) {
                return team1.size() - team2.size();
            }
        });
        int i = 0;
        for (TeamModule team : teams) {
            if (team.isObserver()) continue;
            Team sbTeam = scoreboard.getTeam(team.getId() + "-b");
            setTeamDisplay(sbTeam, (team instanceof PlayerModule ? "" : team.size() + " ") + team.getCompleteName(), team.getColor());
            if (sbTeam.getEntries().size() > 0) {
                if (objective.getScore(new ArrayList<>(sbTeam.getEntries()).get(0)).getScore() != minBlitzScore + i)
                    setScore(objective, new ArrayList<>(sbTeam.getEntries()).get(0), minBlitzScore + i);
            } else {
                String name = team.getColor() + "";
                while (used.contains(name)) {
                    name = team.getColor() + name;
                }
                sbTeam.addEntry(name);
                setScore(objective, name, minBlitzScore + i);
                used.add(name);
            }
            i++;
            currentScore++;
        }
    }

    public void setTeamDisplay(Team team, String display, ChatColor entry) {
        setTeamDisplay(team, display, entry, false);
    }

    public void setTeamDisplay(Team team, String display, ChatColor entry, boolean entryHalfColor) {
        Pair<String, String> pair = Strings.split(display, entry, entryHalfColor);
        team.setPrefix(pair.getLeft());
        team.setSuffix(pair.getRight());
    }

}
