package in.twizmwaz.cardinal.cycle;

import in.twizmwaz.cardinal.Cardinal;
import org.bukkit.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;

public class CycleTimer implements Runnable, Cancellable {

    private Cycle cycle;
    private int time;
    private boolean cancelled;
    private MatchState originalState;

    public CycleTimer(Cycle cycle, int time) {
        this.cycle = cycle;
        this.time = time;
        this.cancelled = false;
        this.originalState = GameHandler.getGameHandler().getMatch().getState();
    }

    @Override
    public void run() {
        if (!isCancelled()) {
            GameHandler.getGameHandler().getMatch().setState(MatchState.CYCLING);
            if ((this.time % 5 == 0 && time > 0) || (time < 5 && time > 0)) {
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLING_TIMER, new UnlocalizedChatMessage(ChatColor.AQUA + cycle.getMap().getName() + ChatColor.DARK_AQUA), (this.time == 1 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.DARK_AQUA) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + (this.time + "") + ChatColor.DARK_AQUA)))));
            }
            if (this.time == 0) {
                GameHandler.getGameHandler().cycleAndMakeMatch();
            } else {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), this, 20);
            }
            this.time--;
        } else {
            this.setCancelled(false);
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
        if (this.cancelled) {
            GameHandler.getGameHandler().getMatch().setState(originalState);
        }
    }
}
