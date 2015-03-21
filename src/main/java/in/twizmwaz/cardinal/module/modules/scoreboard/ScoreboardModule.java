package in.twizmwaz.cardinal.module.modules.scoreboard;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.ScoreboardUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class ScoreboardModule implements Module {

    private TeamModule team;
    private Scoreboard scoreboard;

    public ScoreboardModule(final TeamModule team) {
        this.team = team;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        for (TeamModule teams : TeamUtils.getTeams()) {
            Team prefixTeam = scoreboard.registerNewTeam(teams.getId());
            prefixTeam.setPrefix(teams.getColor() + "");
            scoreboard.registerNewTeam(teams.getId() + "-t");
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
        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
            scoreboard.getScoreboard().getTeam(team.getId()).addPlayer(player);
        }
    }

    public static void remove(TeamModule team, Player player) {
        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
            scoreboard.getScoreboard().getTeam(team.getId()).removePlayer(player);
        }
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        create();
    }

    public void create() {
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        int slot = 0;
        int hills = getSpecificObjective().equals(HillObjective.class) && !ScoreModule.matchHasScoring() ? 0 : -1;
        if (getSlots() < 16) {
            for (TeamModule team : TeamUtils.getTeams()) {
                if (!team.isObserver() && team != this.team) {
                    for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                        Team objTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                        // objTeam.add();
                    }
                }
            }
        } else {
            if (getCompactSlots() < 16) {

            }
        }
    }

    public String getNextBlankSlot(List<String> used) {
        String blank = " ";
        while (used.contains(blank)) {
            blank += " ";
        }
        return blank;
    }

    public void setScore(Objective objective, String string, int score) {
        if (score == 0) {
            objective.getScore(string).setScore(1);
        }
        objective.getScore(string).setScore(score);
    }

    public int getSlots() {
        int slots = 0;
        for (TeamModule team : TeamUtils.getTeams()) {
            if (!team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                slots += 2;
                slots += TeamUtils.getShownObjectives(team).size();
            }
        }
        if (ScoreModule.matchHasScoring()) slots += (TeamUtils.getTeams().size() - 1);
        if (Blitz.matchIsBlitz()) slots += (TeamUtils.getTeams().size() - 1);
        if (ScoreModule.matchHasMax()) slots ++;
        if (ScoreboardUtils.getHills().size() > 0) {
            slots ++;
            slots += ScoreboardUtils.getHills().size();
        }
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
        if (ScoreModule.matchHasScoring()) slots += (TeamUtils.getTeams().size() - 1);
        if (Blitz.matchIsBlitz()) slots += (TeamUtils.getTeams().size() - 1);
        if (ScoreModule.matchHasMax()) slots ++;
        if (ScoreboardUtils.getHills().size() > 0) {
            slots += 2;
        }
        slots --;
        return slots;
    }

    public String getDisplayTitle() {
        boolean hasObjectives = GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class).size() > 0;
        Class objective = getSpecificObjective();
        if (hasObjectives) {
            if (objective != null) {
                if (objective.equals(WoolObjective.class)) {
                    return ChatColor.GOLD + "Wools";
                } else if (objective.equals(CoreObjective.class)) {
                    return ChatColor.GOLD + "Cores";
                } else if (objective.equals(DestroyableObjective.class)) {
                    return ChatColor.GOLD + "Monuments";
                } else if (objective.equals(HillObjective.class)) {
                    return ChatColor.GOLD + "Hills";
                }
            } else {
                return ChatColor.GOLD + "Objectives";
            }
        } else if (GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class) != null) {
            return ChatColor.GOLD + GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class).getTitle();
        } else if (ScoreModule.matchHasScoring()) {
            return ChatColor.GOLD + "Scores";
        }
        return ChatColor.RED + "" + ChatColor.BOLD + "Invalid";
    }

    public Class getSpecificObjective() {
        Class objective = null;
        for (TeamModule team : TeamUtils.getTeams()) {
            for (GameObjective gameObjective : TeamUtils.getShownObjectives(team)) {
                if (objective == null) {
                    objective = gameObjective.getClass();
                } else {
                    objective = null;
                }
            }
        }
        for (GameObjective gameObjective : ScoreboardUtils.getHills()) {
            if (objective == null) {
                objective = gameObjective.getClass();
            } else {
                objective = null;
            }
        }
        return objective;
    }

}
