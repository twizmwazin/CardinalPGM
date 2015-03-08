package in.twizmwaz.cardinal.module.modules.cycleTimer;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.util.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class CycleTimerModule implements TaskedModule, Cancellable {

    private boolean cancelled = true;
    private MatchState originalState;
    private int time;

    private Match match;

    public CycleTimerModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (!isCancelled()) {
            match.setState(MatchState.CYCLING);
            if ((time % 100 == 0 && time > 0) || (time < 100 && time > 0 && time % 20 == 0)) {
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLING_TIMER, new UnlocalizedChatMessage(ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName() + ChatColor.DARK_AQUA), (this.time == 1 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.DARK_AQUA) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + (time / 20 + "") + ChatColor.DARK_AQUA)))));
            }
            if (time == 0 && match.getState() == MatchState.CYCLING) {
                cancelled = true;
                GameHandler.getGameHandler().cycleAndMakeMatch();
            }
            time--;
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        int time = Cardinal.getInstance().getConfig().getInt("cycle");
        if (time < 0) {
            return;
        }
        startTimer(time);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time * 20;
    }

    public MatchState getOriginalState() {
        return originalState;
    }

    public void setOriginalState(MatchState state) {
        this.originalState = state;
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

    public boolean startTimer(int seconds) {
        if (match.getState() != MatchState.PLAYING) {
            setOriginalState(match.getState());
            setTime(seconds);
            setCancelled(false);
            return true;
        } else return false;
    }

}
