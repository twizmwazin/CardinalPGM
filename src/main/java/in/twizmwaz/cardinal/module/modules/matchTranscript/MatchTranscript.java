package in.twizmwaz.cardinal.module.modules.matchTranscript;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    	SimpleDateFormat format;
    	if (Cardinal.getInstance().getConfig().getBoolean("html.transcriptMilliseconds")) {
    		format = new SimpleDateFormat("HH:mm:ss.SSS");
    	} else {
    		format = new SimpleDateFormat("HH:mm:ss");
    	}
        log += "[" + format.format(new Date()) + "] " + ChatColor.stripColor(string) + "\n";
    }

    @EventHandler
     public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        if (event.getObjective().showOnScoreboard()) {
            if (event.getObjective() instanceof DestroyableObjective) {
                log(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DESTROYED, event.getObjective().getTeam().getName(), event.getObjective().getName(), ((DestroyableObjective) event.getObjective()).getWhoDestroyed(Locale.getDefault().toString())).getMessage(Locale.getDefault().toString()));
            }
        }
    }

    @EventHandler
    public void onObjectiveTouch(ObjectiveTouchEvent event) {
        if (event.getObjective().showOnScoreboard() && event.displayTouchMessage()) {
            if (event.getObjective() instanceof WoolObjective) {
                log(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED_FOR, event.getPlayer().getName(), event.getObjective().getName().toUpperCase().replaceAll("_", " "), TeamUtils.getTeamByPlayer(event.getPlayer()).getName()).getMessage(Locale.getDefault().toString()));
            } else if (event.getObjective() instanceof CoreObjective) {
                log(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_TOUCHED_FOR, event.getPlayer().getName(), event.getObjective().getName(), TeamUtils.getTeamByPlayer(event.getPlayer()).getName()).getMessage(Locale.getDefault().toString()));
            } else if (event.getObjective() instanceof DestroyableObjective) {
                log(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DAMAGED_FOR, event.getPlayer().getName(), event.getObjective().getName(), TeamUtils.getTeamByPlayer(event.getPlayer()).getName()).getMessage(Locale.getDefault().toString()));
            }
        }
    }
}
