package in.twizmwaz.cardinal.module.modules.matchTranscript;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MatchTranscript implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    private final File logFile = new File(GameHandler.getGameHandler().getMatchFile() + "/log.txt");
    private PrintWriter writer;
    private String log;


    protected MatchTranscript() {
        if (!logFile.exists())
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().warning("Unable to create a match transcript file");
            }
        try {
            writer = new PrintWriter(logFile);
        } catch (FileNotFoundException e) {
                Bukkit.getLogger().warning("Unable to find the match transcript file");
        }
    }

    public File getLogFile() {
        return logFile;
    }

    public String getLog() {
        return log;
    }

    public void log(String string) {
    	SimpleDateFormat format;
    	if (Cardinal.getInstance().getConfig().getBoolean("html.transcriptMilliseconds")) {
    		format = new SimpleDateFormat("HH:mm:ss.SSS");
    	} else {
    		format = new SimpleDateFormat("HH:mm:ss");
    	}
    		
        writer.println("[" + format.format(new Date()) + "] " + string);
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

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        log(event.getTeam() != null ? event.getTeam().getName() + " won the match!" : "Match ended with no winner" );
        writer.close();
        try {
            FileInputStream in = new FileInputStream(logFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String lines = "";
            String line;
            while ((line = reader.readLine()) != null) {
                lines += line + "\n";
            }
            log = lines;
        } catch (IOException e) {
            Bukkit.getLogger().warning("Error when logging to transcript");
        }
    }
}
