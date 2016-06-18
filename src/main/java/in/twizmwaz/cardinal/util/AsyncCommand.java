package in.twizmwaz.cardinal.util;

import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.command.CommandSender;

public abstract class AsyncCommand implements Runnable {

    public final CommandContext args;
    public final CommandSender sender;

    public AsyncCommand(final CommandContext args, final CommandSender sender) {
        this.args = args;
        this.sender = sender;
    }

}