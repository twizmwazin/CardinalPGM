package in.twizmwaz.cardinal.match.util;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.Bukkit;


/**
 * Created by kevin on 11/19/14.
 */
public class StartTimer implements Runnable {

    int time;
    Match match;

    public StartTimer(GameHandler gameHandler, int seconds) {
        this.time = seconds;
        this.match = gameHandler.getMatch();
    }

    @Override
    public void run() {
        if ((time % 5 == 0 && time > 0) || (time < 5 && time > 0)) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "Match starting in " + ChatColor.DARK_RED + time + ChatColor.GREEN + " seconds");
        }
        time--;
        if (time == 0) {

            if (match.getState() != MatchState.STARTING){
                return;
            } else {
                match.setState(MatchState.PLAYING);
                Bukkit.broadcastMessage(ChatColor.GREEN + "The match has started!");
                Bukkit.getServer().getPluginManager().callEvent(new MatchStartEvent());
            }

        }


    }
}
