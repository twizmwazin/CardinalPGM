package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.timers.Countdown;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CancelCommand {

    @Command(aliases = {"cancel"}, desc = "Cancels the current countdown.")
    @CommandPermissions("cardinal.cancel")
    public static void cancel(final CommandContext cmd, CommandSender sender) {
        Countdown.stopCountdowns(GameHandler.getGameHandler().getMatch());
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_COUNTDOWN_CANELLED)));
    }
}
