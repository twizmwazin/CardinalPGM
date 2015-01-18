package in.twizmwaz.cardinal.module.modules.gameScoreboard;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
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

    private TeamModule team;
    private Scoreboard scoreboard;

    protected GameScoreboard(TeamModule team) {
        this.team = team;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        for (TeamModule teamModule : TeamUtils.getTeams()) {
            Team scoreboardTeam = scoreboard.registerNewTeam(teamModule.getId());
            scoreboardTeam.setPrefix(team.getColor() + "");
            scoreboard.registerNewTeam(teamModule.getId() + "-s");
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

    public static void add(TeamModule teamModule, Player player) {
        for (GameScoreboard gameScoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(GameScoreboard.class)) {
            gameScoreboard.getScoreboard().getTeam(teamModule.getId()).addPlayer(player);
        }
    }

    public static void remove(TeamModule teamModule, Player player) {
        for (GameScoreboard gameScoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(GameScoreboard.class)) {
            gameScoreboard.getScoreboard().getTeam(teamModule.getId()).removePlayer(player);
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
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (event.getNewTeam().equals(this.team)) {
            event.getPlayer().setScoreboard(this.scoreboard);
        }
        for (TeamModule teamModule : TeamUtils.getTeams()) {
            remove(teamModule, event.getPlayer());
        }
        add(event.getNewTeam(), event.getPlayer());
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
                if (!team.equals(this.team) && !team.isObserver()) {
                    for (GameObjective gameObjective : TeamUtils.getObjectives(team)) {
                        if (gameObjective.showOnScoreboard()) {
                            String insert = "";
                            while (used.contains(ScoreboardUtils.getConversion(gameObjective.getScoreboardHandler().getPrefix() + " " + ChatColor.RESET + WordUtils.capitalizeFully(gameObjective.getName()), insert, true))) {
                                insert += ChatColor.RESET;
                            }
                            String obj = ScoreboardUtils.convertToScoreboard(scoreboard.getTeam(gameObjective.getScoreboardHandler().getNumber() + "-o"), gameObjective.getScoreboardHandler().getPrefix() + " " + ChatColor.RESET + WordUtils.capitalizeFully(gameObjective.getName()), insert, true);
                            used.add(obj);
                            objective.getScore(obj).setScore(slot);
                            slot ++;
                        }
                    }
                    Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                    String insert = team.getColor() + "";
                    while (used.contains(ScoreboardUtils.getConversion(team.getName(), insert, true))) {
                        insert += ChatColor.RESET;
                    }
                    String steam = ScoreboardUtils.convertToScoreboard(scoreboardTeam, team.getName(), insert, true);
                    used.add(steam);
                    objective.getScore(steam).setScore(slot);
                    slot ++;
                    if (!this.team.isObserver() || !TeamUtils.getTeams().get(TeamUtils.getTeams().size() - 1).equals(team)) {
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
            if (!team.isObserver()) {
                for (GameObjective gameObjective : TeamUtils.getObjectives(team)) {
                    if (gameObjective.showOnScoreboard()) {
                        String insert = "";
                        while (used.contains(ScoreboardUtils.getConversion(gameObjective.getScoreboardHandler().getPrefix() + " " + ChatColor.RESET + WordUtils.capitalizeFully(gameObjective.getName()), insert, true))) {
                            insert += ChatColor.RESET;
                        }
                        String obj = ScoreboardUtils.convertToScoreboard(scoreboard.getTeam(gameObjective.getScoreboardHandler().getNumber() + "-o"), gameObjective.getScoreboardHandler().getPrefix() + " " + ChatColor.RESET + WordUtils.capitalizeFully(gameObjective.getName()), insert, true);
                        used.add(obj);
                        objective.getScore(obj).setScore(slot);
                        slot ++;
                    }
                }
                Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                String insert = team.getColor() + "" + ChatColor.ITALIC;
                while (used.contains(ScoreboardUtils.getConversion(team.getName(), insert, true))) {
                    insert += ChatColor.RESET;
                }
                String steam = ScoreboardUtils.convertToScoreboard(scoreboardTeam, team.getName(), insert, true);
                used.add(steam);
                objective.getScore(steam).setScore(slot);
            }
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public int getSlots() {
        int slots = 0;
        for (TeamModule team : TeamUtils.getTeams()) {
            slots += 2;
            slots += TeamUtils.getObjectives(team).size();
        }
        slots --;
        return slots;
    }
}
