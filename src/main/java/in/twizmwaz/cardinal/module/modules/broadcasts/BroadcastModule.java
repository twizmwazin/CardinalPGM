package in.twizmwaz.cardinal.module.modules.broadcasts;

import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;

public class BroadcastModule implements TaskedModule {

    public enum BroadcastType {
        TIP(), ALERT();
    }

    private final String message;
    private final BroadcastType type;
    private final int timeAfter;
    private final int every;
    private final int count;
    private int timesBroadcasted;

    protected BroadcastModule(final String message, final BroadcastType type, final int timeAfter, final int every, final int count) {
        this.message = message;
        this.type = type;
        this.timeAfter = timeAfter;
        this.every = every;
        this.count = count;

        this.timesBroadcasted = 0;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (timesBroadcasted < count) {
            if (MatchTimer.getTimeInSeconds() >= (timeAfter + (every * timesBroadcasted))) {
                if (type.equals(BroadcastType.TIP)) Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.BLUE + "" + ChatColor.BOLD + "Tip" + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "] " + ChatColor.AQUA + "" + ChatColor.ITALIC + message);
                else if (type.equals(BroadcastType.ALERT)) Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Alert" + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "] " + ChatColor.RED + "" + ChatColor.ITALIC + message);
                timesBroadcasted ++;
            }
        }
    }


}
