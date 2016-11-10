package in.twizmwaz.cardinal.module.modules.timers;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Config;
import in.twizmwaz.cardinal.util.bossBar.BossBars;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.EventHandler;

public class CycleTimer extends Countdown {

    private boolean hasPlayed = false;
    private Match match;

    public CycleTimer(Match match) {
        super(BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.BLUE, BarStyle.SOLID, false));
        this.match = match;
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        this.hasPlayed = true;
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        if (Config.cycleAuto >= 0) {
            startCountdown(Config.cycleAuto);
        }
    }

    @Override
    public ChatMessage getBossbarMessage() {
        return new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLING_TIMER,
                new UnlocalizedChatMessage(ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName() + ChatColor.DARK_AQUA),
                new LocalizedChatMessage(getTime() == 1 ? ChatConstant.UI_SECOND : ChatConstant.UI_SECONDS, ChatColor.DARK_RED + "" + getTime() + ChatColor.DARK_AQUA)));
    }

    @Override
    public ChatMessage getBossbarEndMessage() {
        return new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName()));
    }

    @Override
    public void onCountdownStart() {
        if (getTime() >= 1) ChatUtil.getGlobalChannel().sendLocalizedMessage(getBossbarMessage());
        match.setState(MatchState.CYCLING);
    }

    @Override
    public boolean canStart() {
        return !match.isRunning();
    }

    @Override
    public void onCountdownCancel() {
        match.setState(hasPlayed ? MatchState.ENDED : MatchState.WAITING);
    }

    @Override
    public void onCountdownEnd() {
        GameHandler.getGameHandler().cycleAndMakeMatch();
    }

}