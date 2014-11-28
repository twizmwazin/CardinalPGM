package in.twizmwaz.cardinal.cycle;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.Bukkit;

/**
 * Created by kevin on 11/27/14.
 */
public class CycleTimer implements Runnable {

    private Cycle cycle;
    private int time;

    public CycleTimer(Cycle cycle, int time) {
        this.cycle = cycle;
        this.time = time;
    }

    @Override
    public void run() {
        if ((time % 5 == 0 && time > 0) || (time < 5 && time > 0)) {
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Cycling to " + ChatColor.AQUA + cycle.getMap() + ChatColor.DARK_AQUA + " in " + ChatColor.DARK_RED + time + ChatColor.DARK_AQUA + " seconds");
        }
        time--;
        if (time == 0) {
            GameHandler.getGameHandler().cycleAndMakeMatch();
        }

    }

}
