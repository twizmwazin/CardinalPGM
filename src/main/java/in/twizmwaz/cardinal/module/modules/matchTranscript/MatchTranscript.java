package in.twizmwaz.cardinal.module.modules.matchTranscript;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MatchTranscript implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    private String log;


    protected MatchTranscript() {
        log = "";
    }

    public String getLog() {
        return log;
    }

    public void log(String string) {
        log += "[" + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()) + "] " + string + "\n";
    }

    @EventHandler
     public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        if (event.getObjective() != null)
            log(event.getPlayer().getName() + " completed " + event.getObjective().getName() + " for " + TeamUtils.getTeamByPlayer(event.getPlayer()).getName());
    }

    @EventHandler
    public void onObjectiveTouch(ObjectiveTouchEvent event) {
        log(event.getPlayer().getName() + " touched " + event.getObjective().getName() + " for " + TeamUtils.getTeamByPlayer(event.getPlayer()).getName());
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        log("Match has started on " + GameHandler.getGameHandler().getMatch().getLoadedMap().getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMatchEnd(MatchEndEvent event) {
        log(event.getTeam() != null ? event.getTeam().getName() + " won the match!" : "Match ended with no winner");
    }
}
