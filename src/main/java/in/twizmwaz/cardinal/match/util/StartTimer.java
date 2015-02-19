package in.twizmwaz.cardinal.match.util;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.java.JavaPlugin;

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
                ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTING_IN, time == 1 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.GREEN) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + (time + "") + ChatColor.GREEN))));
            }
            if (time == 0) {
                if (match.getState() != MatchState.STARTING) {
                    return;
                } else {
                    match.setState(MatchState.PLAYING);
                    ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTED)));
                    Bukkit.getServer().getPluginManager().callEvent(new MatchStartEvent());
                }
            } else {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20);
            }
            if (time <= 3 && time >= 1) for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            if (time == 0) for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
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
}
