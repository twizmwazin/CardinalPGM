package in.twizmwaz.cardinal.match.util;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Created by kevin on 11/19/14.
 */
public class StartTimer implements Runnable, Cancellable {

    private int time;
    private Match match;
    private boolean cancelled;
    private JavaPlugin plugin;

    public StartTimer(Match match, int seconds) {
        this.time = seconds;
        this.match = match;
        this.plugin = GameHandler.getGameHandler().getPlugin();
    }

    @Override
    public void run() {
        if (!isCancelled()) {


            if ((time % 5 == 0 && time > 0) || (time < 5 && time > 0)) {
                Bukkit.broadcastMessage(ChatColor.GREEN + "Match starting in " + ChatColor.DARK_RED + time + ChatColor.GREEN + " seconds");
            }

            if (time == 0) {

                if (match.getState() != MatchState.STARTING) {
                    return;
                } else {
                    match.setState(MatchState.PLAYING);
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The match has started!");
                    Bukkit.getServer().getPluginManager().callEvent(new MatchStartEvent());
                }

            }
            time--;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20);
        } else {
            this.setCancelled(false);
            match.setState(MatchState.WAITING);
        }

    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
