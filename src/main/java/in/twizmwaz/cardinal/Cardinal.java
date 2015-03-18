package in.twizmwaz.cardinal;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocaleHandler;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.command.*;
import in.twizmwaz.cardinal.demographics.DemographicsHandler;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import in.twizmwaz.cardinal.settings.Setting;
import in.twizmwaz.cardinal.settings.SettingValue;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Cardinal extends JavaPlugin {

    private static Cardinal instance;
    private static GameHandler gameHandler;
    private static LocaleHandler localeHandler;
    private CommandsManager<CommandSender> commands;
    private static Database database;
    //private DemographicsHandler demographicsHandler;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String locale = ChatUtils.getLocale(sender);
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NO_PERMISSION).getMessage(locale));
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage().replace("{cmd}", cmd.getName()));
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NUMBER_STRING).getMessage(locale));
            } else {
                sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_UNKNOWN_ERROR).getMessage(locale));
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
        cmdRegister.register(MapCommands.class);
        cmdRegister.register(MatchCommand.class);
        cmdRegister.register(StartAndEndCommand.class);
        cmdRegister.register(JoinCommand.class);
        cmdRegister.register(RotationCommands.class);
        cmdRegister.register(CancelCommand.class);
        cmdRegister.register(TeamCommand.class);
        cmdRegister.register(ModesCommand.class);
        cmdRegister.register(ClassCommands.class);
        cmdRegister.register(CardinalCommand.class);
        cmdRegister.register(ChatCommands.class);
        cmdRegister.register(SettingCommands.class);
        cmdRegister.register(WhitelistCommands.WhitelistParentCommand.class);
        cmdRegister.register(SnowflakesCommand.class);
        cmdRegister.register(TeleportCommands.class);
        cmdRegister.register(PrivateMessageCommands.class);
        cmdRegister.register(ModeratorCommand.class);
        cmdRegister.register(PunishmentCommands.class);
        cmdRegister.register(StatsCommand.class);
        cmdRegister.register(ReadyCommand.class);
        cmdRegister.register(ListCommand.class);
        cmdRegister.register(TimeLimitCommand.class);
        cmdRegister.register(ScoreCommand.class);
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            localeHandler = new LocaleHandler(this);
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            this.setEnabled(false);
            return;
        }
        File databaseFile = new File(getDataFolder(), "database.xml");
        if (databaseFile.exists()) {
            try {
                database = Database.loadFromFile(databaseFile);
            } catch (JDOMException | IOException e) {
                e.printStackTrace();
                Bukkit.getLogger().log(Level.SEVERE, "CardinalPGM failed to initialize because of an IOException. Please try restarting your server.");
                this.setEnabled(false);
                return;
            }
        } else {
            database = Database.newInstance(databaseFile);
        }
        //this.demographicsHandler = new DemographicsHandler(); //Disabled until issues can be resolved
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        if (config.getBoolean("deleteMatches")) {
            Bukkit.getLogger().log(Level.INFO, "[CardinalPGM] Deleting match files, this can be disabled via the configuration");
            File matches = new File("matches/");
            try {
                FileUtils.deleteDirectory(matches);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (config.contains("settings")) {
            for (String settingName : config.getStringList("settings")) {
                List<String> names = new ArrayList<>();
                String description = "No description.";
                List<SettingValue> values = new ArrayList<>();
                names.add(settingName.trim());
                if (config.contains("setting." + settingName + ".aliases")) {
                    for (String alias : config.getStringList("setting." + settingName + ".aliases")) {
                        names.add(alias.trim());
                    }
                }
                if (config.contains("setting." + settingName + ".description")) description = config.getString("setting." + settingName + ".description");
                if (config.contains("setting." + settingName + ".values")) {
                    for (String valueName : config.getStringList("setting." + settingName + ".values")) {
                        if (valueName.endsWith("[default]")) values.add(new SettingValue(valueName.trim().substring(0, valueName.length() - 9), true));
                        else values.add(new SettingValue(valueName.trim(), false));
                    }
                }
                new Setting(names, description, values);
            }
        }
        try {
            gameHandler = new GameHandler();
        } catch (RotationLoadException e) {
            Bukkit.getLogger().log(Level.SEVERE, "CardinalPGM failed to initialize because of an invalid rotation configuration.");
            setEnabled(false);
            return;
        }
        setupCommands();
    }
    
    @Override
    public void onDisable() {
        database.save(new File(getDataFolder().getAbsolutePath() + "/database.xml"));
        //demographicsHandler.saveAndReport();
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public static LocaleHandler getLocaleHandler() {
        return localeHandler;
    }

    public JavaPlugin getPlugin() {
        return this;
    }

    public static Cardinal getInstance() {
        return instance;
    }
    
    public static Database getCardinalDatabase() {
        return database;
    }
}
