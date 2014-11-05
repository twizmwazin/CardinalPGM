package in.twizmwaz.cardinal;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import in.twizmwaz.cardinal.cycle.CycleCommand;
import in.twizmwaz.cardinal.cycle.RespawnListener;
import in.twizmwaz.cardinal.listeners.BlockListener;
import in.twizmwaz.cardinal.listeners.ConnectionListener;
import in.twizmwaz.cardinal.listeners.EntityListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by kevin on 11/1/14.
 */
public class Cardinal extends JavaPlugin {

    public static JavaPlugin twizPGM;
    public static GameHandler gameHandler;
    private CommandsManager<CommandSender> commands;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);
        cmdRegister.register(CycleCommand.class);
    }

    public void registerListeners() {
        new BlockListener(this);
        new ConnectionListener(this);
        new EntityListener(this);
        new ConnectionListener(this);
        new RespawnListener(this);
    }


    public void onEnable() {
        setupCommands();
        registerListeners();
        gameHandler = new GameHandler();
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

}
