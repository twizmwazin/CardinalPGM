package in.twizmwaz.cardinal.module.modules.startTimer;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class StartTimer implements TaskedModule, Cancellable {

    private int time;
    private Match match;
    private boolean cancelled;

    public StartTimer(Match match, int ticks) {
        this.time = ticks;
        this.match = match;
        this.cancelled = true;
    }

    @Override
    public void run() {
        if (!isCancelled()) {
            if ((time % 100 == 0 && time > 0) || (time < 100 && time > 0 && time % 20 == 0)) {
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTING_IN, time == 20 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.GREEN) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + "" + (time / 20) + "" + ChatColor.GREEN))));
            }
            if (time == 0) {
                if (match.getState() != MatchState.STARTING) {
                    return;
                } else {
                    match.setState(MatchState.PLAYING);
                    ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTED)));
                    Bukkit.getServer().getPluginManager().callEvent(new MatchStartEvent());
                }
            }
            if (time <= 60 && time >= 20 && time % 20 == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    }
                }
            }
            if (time == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
                    }
                }
            }
            time--;
        }

    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
        if (this.cancelled && GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            GameHandler.getGameHandler().getMatch().setState(MatchState.WAITING);
        }
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
