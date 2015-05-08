package in.twizmwaz.cardinal.module.modules.scoreboard;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.*;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveProximityEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
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
import in.twizmwaz.cardinal.util.StringUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
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
        if (ScoreModule.matchHasScoring()) {
            for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                scoreboard.registerNewTeam(score.getTeam().getId() + "-s");
            }
        }
        if (Blitz.matchIsBlitz()) {
            for (TeamModule teams : TeamUtils.getTeams()) {
                if (!teams.isObserver()) {
                    scoreboard.registerNewTeam(teams.getId() + "-b");
                }
            }
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
            if (event.getNewTeam() == this.team) {
                event.getPlayer().setScoreboard(this.scoreboard);
            }
            for (TeamModule team : TeamUtils.getTeams()) {
                remove(team, event.getPlayer());
            }
            add(event.getNewTeam(), event.getPlayer());

            if (Blitz.matchIsBlitz()) {
                if (event.getOldTeam() != null && !event.getOldTeam().isObserver()) {
                    TeamModule team = event.getOldTeam();
                    Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-b");
                    for (String entry : scoreboardTeam.getEntries()) {
                        setScore(scoreboard.getObjective("scoreboard"), entry, team.size());
                    }
                }
                if (event.getNewTeam() != null && !event.getNewTeam().isObserver()) {
                    TeamModule team = event.getNewTeam();
                    Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-b");
                    for (String entry : scoreboardTeam.getEntries()) {
                        setScore(scoreboard.getObjective("scoreboard"), entry, team.size());
                    }
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

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        create();
    }

    @EventHandler
    public void onObjectiveProximity(ObjectiveProximityEvent event) {
        if (getSlots() < 16) {
            Team scoreboardTeam = scoreboard.getTeam(event.getObjective().getScoreboardHandler().getNumber() + "-o");
            String prefix = event.getObjective().getScoreboardHandler().getPrefix(this.team).length() > 16 ? event.getObjective().getScoreboardHandler().getPrefix(this.team).substring(0, 16) : event.getObjective().getScoreboardHandler().getPrefix(this.team);
            scoreboardTeam.setPrefix(prefix);
        } else {
            if (getCompactSlots() < 16) {
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver() && TeamUtils.getShownObjectives(team).contains(event.getObjective())) {
                        Team scoreboardTeam = null;
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                            break;
                        }
                        String compact = "";
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            compact += obj.getScoreboardHandler().getPrefix(this.team) + " ";
                        }
                        if (scoreboardTeam != null) {
                            while (compact.length() > 32) {
                                compact = StringUtils.removeLastWord(compact);
                            }
                            if (compact.charAt(15) == '\u00A7') {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 15));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 15, 31));
                            } else {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 16));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 16, 32));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTimeLimitChange(TimeLimitChangeEvent event) {
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (getSlots() < 16) {
                Team scoreboardTeam = scoreboard.getTeam(objective.getScoreboardHandler().getNumber() + "-o");
                String prefix = objective.getScoreboardHandler().getPrefix(this.team).length() > 16 ? objective.getScoreboardHandler().getPrefix(this.team).substring(0, 16) : objective.getScoreboardHandler().getPrefix(this.team);
                scoreboardTeam.setPrefix(prefix);
            } else {
                if (getCompactSlots() < 16) {
                    for (TeamModule team : TeamUtils.getTeams()) {
                        if (!team.isObserver() && TeamUtils.getShownObjectives(team).contains(objective)) {
                            Team scoreboardTeam = null;
                            for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                                scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                                break;
                            }
                            String compact = "";
                            for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                                compact += obj.getScoreboardHandler().getPrefix(this.team) + " ";
                            }
                            if (scoreboardTeam != null) {
                                while (compact.length() > 32) {
                                    compact = StringUtils.removeLastWord(compact);
                                }
                                if (compact.charAt(15) == '\u00A7') {
                                    scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 15));
                                    scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 15, 31));
                                } else {
                                    scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 16));
                                    scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 16, 32));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onObjectiveTouch(ObjectiveTouchEvent event) {
        if (getSlots() < 16) {
            Team scoreboardTeam = scoreboard.getTeam(event.getObjective().getScoreboardHandler().getNumber() + "-o");
            String prefix = event.getObjective().getScoreboardHandler().getPrefix(this.team).length() > 16 ? event.getObjective().getScoreboardHandler().getPrefix(this.team).substring(0, 16) : event.getObjective().getScoreboardHandler().getPrefix(this.team);
            scoreboardTeam.setPrefix(prefix);
        } else {
            if (getCompactSlots() < 16) {
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver() && TeamUtils.getShownObjectives(team).contains(event.getObjective())) {
                        Team scoreboardTeam = null;
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                            break;
                        }
                        String compact = "";
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            compact += obj.getScoreboardHandler().getPrefix(this.team) + " ";
                        }
                        if (scoreboardTeam != null) {
                            while (compact.length() > 32) {
                                compact = StringUtils.removeLastWord(compact);
                            }
                            if (compact.charAt(15) == '\u00A7') {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 15));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 15, 31));
                            } else {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 16));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 16, 32));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        if (getSlots() < 16) {
            Team scoreboardTeam = scoreboard.getTeam(event.getObjective().getScoreboardHandler().getNumber() + "-o");
            String prefix = event.getObjective().getScoreboardHandler().getPrefix(this.team).length() > 16 ? event.getObjective().getScoreboardHandler().getPrefix(this.team).substring(0, 16) : event.getObjective().getScoreboardHandler().getPrefix(this.team);
            scoreboardTeam.setPrefix(prefix);
        } else {
            if (getCompactSlots() < 16) {
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver() && TeamUtils.getShownObjectives(team).contains(event.getObjective())) {
                        Team scoreboardTeam = null;
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                            break;
                        }
                        String compact = "";
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            compact += obj.getScoreboardHandler().getPrefix(this.team) + " ";
                        }
                        if (scoreboardTeam != null) {
                            while (compact.length() > 32) {
                                compact = StringUtils.removeLastWord(compact);
                            }
                            if (compact.charAt(15) == '\u00A7') {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 15));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 15, 31));
                            } else {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 16));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 16, 32));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTeamNameChange(TeamNameChangeEvent event) {
        if (event.getTeam() == team) {
            Team scoreboardTeam = scoreboard.getTeam(event.getTeam().getId() + "-t");
            scoreboardTeam.setPrefix(event.getTeam().getColor() + "" + ChatColor.ITALIC + StringUtils.trimTo(event.getTeam().getName(), 0, 12));
            scoreboardTeam.setSuffix(StringUtils.trimTo(event.getTeam().getName(), 12, 28));
            if (ScoreModule.matchHasScoring()) {
                scoreboardTeam = scoreboard.getTeam(event.getTeam().getId() + "-s");
                scoreboardTeam.setPrefix(event.getTeam().getColor() + StringUtils.trimTo(event.getTeam().getName(), 0, 14));
                scoreboardTeam.setSuffix(StringUtils.trimTo(event.getTeam().getName(), 14, 30));
            }
            if (Blitz.matchIsBlitz()) {
                scoreboardTeam = scoreboard.getTeam(event.getTeam().getId() + "-b");
                scoreboardTeam.setPrefix(event.getTeam().getColor() + StringUtils.trimTo(event.getTeam().getName(), 0, 14));
                scoreboardTeam.setSuffix(StringUtils.trimTo(event.getTeam().getName(), 14, 30));
            }
        } else {
            Team scoreboardTeam = scoreboard.getTeam(event.getTeam().getId() + "-t");
            scoreboardTeam.setPrefix(event.getTeam().getColor() + StringUtils.trimTo(event.getTeam().getName(), 0, 14));
            scoreboardTeam.setSuffix(StringUtils.trimTo(event.getTeam().getName(), 14, 30));
            if (ScoreModule.matchHasScoring()) {
                scoreboardTeam = scoreboard.getTeam(event.getTeam().getId() + "-s");
                scoreboardTeam.setPrefix(event.getTeam().getColor() + StringUtils.trimTo(event.getTeam().getName(), 0, 14));
                scoreboardTeam.setSuffix(StringUtils.trimTo(event.getTeam().getName(), 14, 30));
            }
            if (Blitz.matchIsBlitz()) {
                scoreboardTeam = scoreboard.getTeam(event.getTeam().getId() + "-b");
                scoreboardTeam.setPrefix(event.getTeam().getColor() + StringUtils.trimTo(event.getTeam().getName(), 0, 14));
                scoreboardTeam.setSuffix(StringUtils.trimTo(event.getTeam().getName(), 14, 30));
            }
        }
    }

    @EventHandler
    public void onScoreUpdate(ScoreUpdateEvent event) {
        TeamModule team = event.getScoreModule().getTeam();
        Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
        for (String entry : scoreboardTeam.getEntries()) {
            setScore(scoreboard.getObjective("scoreboard"), entry, event.getScoreModule().getScore());
        }
    }

    public void create() {
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        objective.setDisplayName(getDisplayTitle());
        int slot = 0;
        int hills = (getSpecificObjective() != null && getSpecificObjective().equals(HillObjective.class)) && !ScoreModule.matchHasScoring() ? 0 : -1;
        List<String> used = new ArrayList<>();
        if (getSlots() < 16) {





            for (TeamModule team : TeamUtils.getTeams()) {
                if (!team.isObserver() && team != this.team) {
                    for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                        Team scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                        String raw = ChatColor.RESET + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " "));
                        while (used.contains(raw)) {
                            raw = ChatColor.RESET + raw;
                        }
                        String prefix = obj.getScoreboardHandler().getPrefix(this.team).length() > 16 ? obj.getScoreboardHandler().getPrefix(this.team).substring(0, 16) : obj.getScoreboardHandler().getPrefix(this.team);
                        String name = StringUtils.trimTo(raw, 0, 16);
                        scoreboardTeam.setPrefix(prefix);
                        scoreboardTeam.add(name);
                        scoreboardTeam.setSuffix(StringUtils.trimTo(raw, 16, 32));
                        setScore(objective, name, slot);
                        used.add(raw);
                        slot ++;
                    }
                    if (TeamUtils.getShownObjectives(team).size() > 0) {
                        Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-t");
                        String name = team.getColor() + "";
                        while (used.contains(name)) {
                            name = team.getColor() + name;
                        }
                        scoreboardTeam.setPrefix(team.getColor() + StringUtils.trimTo(team.getName(), 0, 14));
                        scoreboardTeam.add(name);
                        scoreboardTeam.setSuffix(StringUtils.trimTo(team.getName(), 14, 30));
                        setScore(objective, name, slot);
                        used.add(name);
                        slot++;
                        if (slot < getObjectiveSlots()) {
                            String blank = getNextBlankSlot(used);
                            setScore(objective, blank, slot);
                            used.add(blank);
                            slot ++;
                        }
                    }
                }
            }
            if (!team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                    Team scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                    String raw = ChatColor.RESET + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " "));
                    while (used.contains(raw)) {
                        raw = ChatColor.RESET + raw;
                    }
                    String prefix = obj.getScoreboardHandler().getPrefix(team).length() > 16 ? obj.getScoreboardHandler().getPrefix(team).substring(0, 16) : obj.getScoreboardHandler().getPrefix(team);
                    String name = StringUtils.trimTo(raw, 0, 16);
                    scoreboardTeam.setPrefix(prefix);
                    scoreboardTeam.add(name);
                    scoreboardTeam.setSuffix(StringUtils.trimTo(raw, 16, 32));
                    setScore(objective, name, slot);
                    used.add(raw);
                    slot++;
                }
                Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-t");
                String name = ChatColor.ITALIC + "";
                while (used.contains(name)) {
                    name = ChatColor.ITALIC + name;
                }
                scoreboardTeam.setPrefix(team.getColor() + "" + ChatColor.ITALIC + StringUtils.trimTo(team.getName(), 0, 12));
                scoreboardTeam.add(name);
                scoreboardTeam.setSuffix(StringUtils.trimTo(team.getName(), 12, 28));
                setScore(objective, name, slot);
                used.add(name);
                slot++;
                if (slot < getObjectiveSlots()) {
                    String blank = getNextBlankSlot(used);
                    setScore(objective, blank, slot);
                    used.add(blank);
                    slot ++;
                }
            }
            if (ScoreModule.matchHasScoring()) {
                for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                    TeamModule team = score.getTeam();
                    Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                    String name = team.getColor() + "";
                    while (used.contains(name)) {
                        name = team.getColor() + name;
                    }
                    scoreboardTeam.setPrefix(team.getColor() + StringUtils.trimTo(team.getName(), 0, 14));
                    scoreboardTeam.add(name);
                    scoreboardTeam.setSuffix(StringUtils.trimTo(team.getName(), 14, 30));
                    setScore(objective, name, score.getScore());
                    used.add(name);
                }
            }
            if (ScoreModule.matchHasMax()) {
                setScore(objective, ChatColor.RED + "---- MAX ----", ScoreModule.max());
            }
            if (Blitz.matchIsBlitz()) {
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-b");
                        String name = team.getColor() + "";
                        while (used.contains(name)) {
                            name = team.getColor() + name;
                        }
                        scoreboardTeam.setPrefix(team.getColor() + StringUtils.trimTo(team.getName(), 0, 14));
                        scoreboardTeam.add(name);
                        scoreboardTeam.setSuffix(StringUtils.trimTo(team.getName(), 14, 30));
                        setScore(objective, name, team.size());
                        used.add(name);
                    }
                }
            }
            if (ScoreboardUtils.getHills().size() > 0) {
                if (ScoreboardUtils.getHills().size() == getSlots()) {
                    for (HillObjective obj : ScoreboardUtils.getHills()) {
                        Team scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                        String raw = ChatColor.RESET + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " "));
                        while (used.contains(raw)) {
                            raw = ChatColor.RESET + raw;
                        }
                        String prefix = obj.getScoreboardHandler().getPrefix(team).length() > 16 ? obj.getScoreboardHandler().getPrefix(team).substring(0, 16) : obj.getScoreboardHandler().getPrefix(team);
                        String name = StringUtils.trimTo(raw, 0, 16);
                        scoreboardTeam.setPrefix(prefix);
                        scoreboardTeam.add(name);
                        scoreboardTeam.setSuffix(StringUtils.trimTo(raw, 16, 32));
                        setScore(objective, name, slot);
                        used.add(raw);
                        slot++;
                    }
                } else {
                    String blank = getNextBlankSlot(used);
                    setScore(objective, blank, hills);
                    used.add(blank);
                    hills --;
                    for (HillObjective obj : ScoreboardUtils.getHills()) {
                        Team scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                        String raw = ChatColor.RESET + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " "));
                        while (used.contains(raw)) {
                            raw = ChatColor.RESET + raw;
                        }
                        String prefix = obj.getScoreboardHandler().getPrefix(team).length() > 16 ? obj.getScoreboardHandler().getPrefix(team).substring(0, 16) : obj.getScoreboardHandler().getPrefix(team);
                        String name = StringUtils.trimTo(raw, 0, 16);
                        scoreboardTeam.setPrefix(prefix);
                        scoreboardTeam.add(name);
                        scoreboardTeam.setSuffix(StringUtils.trimTo(raw, 16, 32));
                        setScore(objective, name, hills);
                        used.add(raw);
                        hills --;
                    }
                }
            }





        } else {
            if (getCompactSlots() < 16) {





                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver() && team != this.team && TeamUtils.getShownObjectives(team).size() > 0) {
                        Team scoreboardTeam = null;
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                            break;
                        }
                        String compact = "";
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            compact += obj.getScoreboardHandler().getPrefix(team) + " ";
                        }
                        if (scoreboardTeam != null) {
                            String name = ChatColor.RESET + "";
                            while (used.contains(name)) {
                                name = ChatColor.RESET + name;
                            }
                            while (compact.length() > 32) {
                                compact = StringUtils.removeLastWord(compact);
                            }
                            scoreboardTeam.add(name);
                            if (compact.charAt(15) == '\u00A7') {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 15));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 15, 31));
                            } else {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 16));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 16, 32));
                            }
                            setScore(objective, name, slot);
                            used.add(name);
                            slot ++;
                        }

                        scoreboardTeam = scoreboard.getTeam(team.getId() + "-t");
                        String name = team.getColor() + "";
                        while (used.contains(name)) {
                            name = team.getColor() + name;
                        }
                        scoreboardTeam.setPrefix(team.getColor() + StringUtils.trimTo(team.getName(), 0, 14));
                        scoreboardTeam.add(name);
                        scoreboardTeam.setSuffix(StringUtils.trimTo(team.getName(), 14, 30));
                        setScore(objective, name, slot);
                        used.add(name);
                        slot++;
                        if (slot < getObjectiveSlots()) {
                            String blank = getNextBlankSlot(used);
                            setScore(objective, blank, slot);
                            used.add(blank);
                            slot ++;
                        }
                    }
                }
                if (!team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                    Team scoreboardTeam = null;
                    for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                        scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                        break;
                    }
                    String compact = "";
                    for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                        compact += obj.getScoreboardHandler().getPrefix(team) + " ";
                    }
                    if (scoreboardTeam != null) {
                        String name = ChatColor.RESET + "";
                        while (used.contains(name)) {
                            name = ChatColor.RESET + name;
                        }
                        while (compact.length() > 32) {
                            compact = StringUtils.removeLastWord(compact);
                        }
                        scoreboardTeam.add(name);
                        if (compact.charAt(15) == '\u00A7') {
                            scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 15));
                            scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 15, 31));
                        } else {
                            scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 16));
                            scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 16, 32));
                        }
                        setScore(objective, name, slot);
                        used.add(name);
                        slot ++;
                    }

                    scoreboardTeam = scoreboard.getTeam(team.getId() + "-t");
                    String name = ChatColor.ITALIC + "";
                    while (used.contains(name)) {
                        name = ChatColor.ITALIC + name;
                    }
                    scoreboardTeam.setPrefix(team.getColor() + "" + ChatColor.ITALIC + StringUtils.trimTo(team.getName(), 0, 12));
                    scoreboardTeam.add(name);
                    scoreboardTeam.setSuffix(StringUtils.trimTo(team.getName(), 12, 28));
                    setScore(objective, name, slot);
                    used.add(name);
                    slot++;
                    if (slot < getObjectiveSlots()) {
                        String blank = getNextBlankSlot(used);
                        setScore(objective, blank, slot);
                        used.add(blank);
                        slot ++;
                    }
                }
                if (ScoreModule.matchHasScoring()) {
                    for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                        TeamModule team = score.getTeam();
                        Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-s");
                        String name = team.getColor() + "";
                        while (used.contains(name)) {
                            name = team.getColor() + name;
                        }
                        scoreboardTeam.setPrefix(team.getColor() + StringUtils.trimTo(team.getName(), 0, 14));
                        scoreboardTeam.add(name);
                        scoreboardTeam.setSuffix(StringUtils.trimTo(team.getName(), 14, 30));
                        setScore(objective, name, score.getScore());
                        used.add(name);
                    }
                }
                if (ScoreModule.matchHasMax()) {
                    setScore(objective, ChatColor.RED + "---- MAX ----", ScoreModule.max());
                }
                if (Blitz.matchIsBlitz()) {
                    for (TeamModule team : TeamUtils.getTeams()) {
                        if (!team.isObserver()) {
                            Team scoreboardTeam = scoreboard.getTeam(team.getId() + "-b");
                            String name = team.getColor() + "";
                            while (used.contains(name)) {
                                name = team.getColor() + name;
                            }
                            scoreboardTeam.setPrefix(team.getColor() + StringUtils.trimTo(team.getName(), 0, 14));
                            scoreboardTeam.add(name);
                            scoreboardTeam.setSuffix(StringUtils.trimTo(team.getName(), 14, 30));
                            setScore(objective, name, team.size());
                            used.add(name);
                        }
                    }
                }
                if (ScoreboardUtils.getHills().size() > 0) {
                    if (ScoreboardUtils.getHills().size() == getSlots()) {
                        Team scoreboardTeam = null;
                        for (HillObjective obj : ScoreboardUtils.getHills()) {
                            scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                            break;
                        }
                        String compact = "";
                        for (HillObjective obj : ScoreboardUtils.getHills()) {
                            compact += obj.getScoreboardHandler().getPrefix(team) + " ";
                        }
                        if (scoreboardTeam != null) {
                            String name = ChatColor.RESET + "";
                            while (used.contains(name)) {
                                name = ChatColor.RESET + name;
                            }
                            while (compact.length() > 32) {
                                compact = StringUtils.removeLastWord(compact);
                            }
                            scoreboardTeam.add(name);
                            if (compact.charAt(15) == '\u00A7') {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 15));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 15, 31));
                            } else {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 16));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 16, 32));
                            }
                            setScore(objective, name, slot);
                            used.add(name);
                        }
                    } else {
                        String blank = getNextBlankSlot(used);
                        setScore(objective, blank, hills);
                        used.add(blank);
                        hills --;
                        Team scoreboardTeam = null;
                        for (HillObjective obj : ScoreboardUtils.getHills()) {
                            scoreboardTeam = scoreboard.getTeam(obj.getScoreboardHandler().getNumber() + "-o");
                            break;
                        }
                        String compact = "";
                        for (HillObjective obj : ScoreboardUtils.getHills()) {
                            compact += obj.getScoreboardHandler().getPrefix(team) + " ";
                        }
                        if (scoreboardTeam != null) {
                            String name = ChatColor.RESET + "";
                            while (used.contains(name)) {
                                name = ChatColor.RESET + name;
                            }
                            while (compact.length() > 32) {
                                compact = StringUtils.removeLastWord(compact);
                            }
                            scoreboardTeam.add(name);
                            if (compact.charAt(15) == '\u00A7') {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 15));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 15, 31));
                            } else {
                                scoreboardTeam.setPrefix(StringUtils.trimTo(compact, 0, 16));
                                scoreboardTeam.setSuffix(StringUtils.trimTo(compact, 16, 32));
                            }
                            setScore(objective, name, hills);
                            used.add(name);
                        }
                    }
                }





            }
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
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
            if (slots != 0) {
                slots ++;
            }
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
            if (slots != 0) {
                slots ++;
            }
            slots ++;
        }
        slots --;
        return slots;
    }

    public int getObjectiveSlots() {
        int slots = 0;
        if (getSlots() < 16) {
            for (TeamModule team : TeamUtils.getTeams()) {
                if (!team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                    slots += 2;
                    slots += TeamUtils.getShownObjectives(team).size();
                }
            }
            slots --;
        } else if (getCompactSlots() < 16) {
            for (TeamModule team : TeamUtils.getTeams()) {
                if (!team.isObserver() && TeamUtils.getShownObjectives(team).size() > 0) {
                    slots += 2;
                    if (TeamUtils.getShownObjectives(team).size() > 0) slots ++;
                }
            }
            slots --;
        }
        return slots;
    }

    public String getDisplayTitle() {
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
                    return ChatColor.AQUA + "Objetives";
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
        boolean checkHills = true;
        for (TeamModule team : TeamUtils.getTeams()) {
            for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
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
            for (GameObjective obj : ScoreboardUtils.getHills()) {
                if (objective == null) {
                    objective = obj.getClass();
                } else if (objective != obj.getClass()) {
                    objective = null;
                }
            }
        }
        return objective;
    }

}
