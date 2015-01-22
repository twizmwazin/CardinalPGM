package in.twizmwaz.cardinal.module.modules.broadcasts;

import in.twizmwaz.cardinal.GameHandler;
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
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (timesBroadcasted < count) {
                if (MatchTimer.getTimeInSeconds() >= (timeAfter + (every * timesBroadcasted))) {
                    if (type.equals(BroadcastType.TIP))
                        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.BLUE + "" + ChatColor.BOLD + "Tip" + ChatColor.GRAY + "" + ChatColor.BOLD + "] " + ChatColor.AQUA + "" + ChatColor.ITALIC + message);
                    else if (type.equals(BroadcastType.ALERT))
                        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.YELLOW + "" + ChatColor.BOLD + "Alert" + ChatColor.GRAY + "" + ChatColor.BOLD + "] " + ChatColor.GREEN + "" + ChatColor.ITALIC + message);
                    timesBroadcasted++;
                }
            }
        }
    }


}
