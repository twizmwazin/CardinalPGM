package in.twizmwaz.cardinal.module.modules.timers;

import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.util.bossBar.BossBars;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public abstract class Countdown implements TaskedModule, Cancellable {

    private boolean cancelled = true, canRun = true, destroyOnEnd;
    private int time, originalTime;
    private UUID bossBar;

    public Countdown(UUID bossbar) {
        this(bossbar, true);
    }

    public Countdown(UUID bossbar, boolean destroyOnEnd) {
        this.bossBar = bossbar;
        this.destroyOnEnd = destroyOnEnd;
    }

    @Override
    public void unload() {
        BossBars.removeBroadcastedBossBar(bossBar);
        HandlerList.unregisterAll(this);
    }

    @Override
    public final void run() {
        if (time < 0 || !canRun) cancelled = true;
        if (!isCancelled()) {
            BossBars.setProgress(bossBar, ((double) time / originalTime));
            if (time % 20 == 0) {
                if (time != 0) {
                    BossBars.setTitle(bossBar, getBossbarMessage());
                    onRun();
                } else {
                    BossBars.setTitle(bossBar, getBossbarEndMessage());
                    BossBars.setProgress(bossBar, 0D);
                    setCancelled(true);
                }
            }
            time--;
        }
    }

    public void onRun() {
    }

    public boolean startCountdown(int time) {
        if (canStart() && time >= 0 ) {
            this.time = time * 20;
            this.originalTime = this.time;
            this.setCancelled(false);
            return true;
        } return false;
    }

    public boolean canStart() {
        return true;
    }

    private boolean endCountdown() {
        if (canEnd()) {
            if (destroyOnEnd) {
                canRun = false;
                BossBars.removeBroadcastedBossBar(bossBar);
            }
            onCountdownEnd();
            return true;
        } return false;
    }

    public boolean canEnd() {
        return true;
    }

    public int getTime() {
        return time / 20;
    }

    // Bossbar messages
    public abstract ChatMessage getBossbarMessage();
    public ChatMessage getBossbarEndMessage() {
        return getBossbarMessage();
    }

    // Actions
    public abstract void onCountdownStart();
    public abstract void onCountdownCancel();
    public abstract void onCountdownEnd();

    @Override
    public final boolean isCancelled() {
        return cancelled;
    }

    @Override
    public final void setCancelled(boolean cancelled) {
        if (this.cancelled ^ cancelled && (canRun || cancelled)) {
            this.cancelled = cancelled;
            if (isCancelled()) {
                BossBars.setVisible(bossBar, false);
                if (time == 0) endCountdown();
                else onCountdownCancel();
            } else {
                onCountdownStart();
                if (time != 0) BossBars.setVisible(bossBar, true);
            }
        }
    }

    public static void stopCountdowns(Match match) {
        match.getModules().getModules(Countdown.class).forEach(timer -> timer.setCancelled(true));
    }

}
