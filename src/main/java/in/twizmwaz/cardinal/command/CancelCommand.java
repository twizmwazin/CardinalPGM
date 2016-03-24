package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.cycleTimer.CycleTimerModule;
import in.twizmwaz.cardinal.module.modules.startTimer.StartTimer;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CancelCommand {

    @Command(aliases = {"cancel"}, desc = "Cancels the current countdown.")
    @CommandPermissions("cardinal.cancel")
    public static void cancel(final CommandContext cmd, CommandSender sender) {
        GameHandler handler = GameHandler.getGameHandler();
        if (!handler.getMatch().getModules().getModule(CycleTimerModule.class).isCancelled()) {
            handler.getMatch().getModules().getModule(CycleTimerModule.class).setCancelled(true);
        }
        if (!handler.getMatch().getModules().getModule(StartTimer.class).isCancelled()) {
            handler.getMatch().getModules().getModule(StartTimer.class).setCancelled(true);
        }
        if (handler.getMatch().getState().equals(MatchState.STARTING)) {
            handler.getMatch().setState(MatchState.WAITING);
        } else if (handler.getMatch().getState().equals(MatchState.CYCLING)) {
            handler.getMatch().setState(MatchState.ENDED);
        }
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_COUNTDOWN_CANELLED)));
    }
}
