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
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.bossBar.BossBars;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class CycleTimerModule implements TaskedModule, Cancellable {

    private boolean cancelled = true;
    private MatchState originalState;
    private int time, originalTime;
    private String bossBar;

    private Match match;

    public CycleTimerModule(Match match) {
        this.match = match;
        this.bossBar = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.BLUE, BarStyle.SOLID, false);
    }

    @Override
    public void unload() {
        BossBars.removeBroadcastedBossBar(bossBar);
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (!isCancelled()) {
            match.setState(MatchState.CYCLING);
            if (time % 20 == 0 && time >= 1) {
                BossBars.setTitle(bossBar, new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLING_TIMER, new UnlocalizedChatMessage(ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName() + ChatColor.DARK_AQUA), (this.time == 1 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.DARK_AQUA) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + (time / 20 + "") + ChatColor.DARK_AQUA)))));
            } else if (time < 1 && time >= 0) {
                BossBars.setTitle(bossBar, new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName())));
            }
            BossBars.setProgress(bossBar, ((double)time / originalTime));
            if ((time % 100 == 0 && time > 0) || (time < 100 && time > 0 && time % 20 == 0)) {
                ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLING_TIMER, new UnlocalizedChatMessage(ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName() + ChatColor.DARK_AQUA), (this.time == 1 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.DARK_AQUA) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + (time / 20 + "") + ChatColor.DARK_AQUA)))));
            }
            if (time == 0 && match.getState() == MatchState.CYCLING) {
                cancelled = true;
                BossBars.setProgress(bossBar, 0D);
                GameHandler.getGameHandler().cycleAndMakeMatch();
            }
            if (time < 0) {
                setCancelled(true);
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
        cycleTimer(time);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time * 20;
    }

    public void setOriginalTime(int time) {
        this.originalTime = time;
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
            BossBars.setVisible(bossBar, false);
        } else {
            BossBars.setVisible(bossBar, true);
        }
    }

    public boolean cycleTimer(int seconds) {
        if (match.getState() != MatchState.PLAYING) {
            setOriginalState(match.getState());
            setTime(seconds);
            setOriginalTime(time);
            setCancelled(false);
            return true;
        } else return false;
    }

}