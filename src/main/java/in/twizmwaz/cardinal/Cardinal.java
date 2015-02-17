package in.twizmwaz.cardinal;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import in.twizmwaz.cardinal.chat.LocaleHandler;
import in.twizmwaz.cardinal.command.*;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import in.twizmwaz.cardinal.settings.Setting;
import in.twizmwaz.cardinal.settings.SettingValue;
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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Cardinal extends JavaPlugin {

    private static Cardinal instance;
    private static GameHandler gameHandler;
    private static LocaleHandler localeHandler;
    private CommandsManager<CommandSender> commands;
    private static Database database;

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
        FileConfiguration config = getConfig();
        config.addDefault("deleteMatches", true);
        if (!config.contains("settings")) {
            config.addDefault("settings", Arrays.asList("Blood", "DeathMessages", "HighlightDeathMessages", "JoinMessages", "Observers", "Picker", "PrivateMessages", "Scoreboard", "Sounds"));
            config.addDefault("setting.Blood.values", Arrays.asList("on", "off[default]"));
            config.addDefault("setting.Blood.description", "See blood when players get hurt");
            config.addDefault("setting.DeathMessages.aliases", Arrays.asList("dms"));
            config.addDefault("setting.DeathMessages.values", Arrays.asList("own", "all[default]"));
            config.addDefault("setting.DeathMessages.description", "Death messages displayed to you");
            config.addDefault("setting.HighlightDeathMessages.aliases", Arrays.asList("hdms"));
            config.addDefault("setting.HighlightDeathMessages.values", Arrays.asList("underline", "italics", "white", "none", "bold[default]"));
            config.addDefault("setting.HighlightDeathMessages.description", "Highlight death messages that you are involved in");
            config.addDefault("setting.JoinMessages.aliases", Arrays.asList("jms"));
            config.addDefault("setting.JoinMessages.values", Arrays.asList("none", "all[default]"));
            config.addDefault("setting.JoinMessages.description", "Join messages displayed to you");
            config.addDefault("setting.Observers.aliases", Arrays.asList("obs"));
            config.addDefault("setting.Observers.values", Arrays.asList("none", "all[default]"));
            config.addDefault("setting.Observers.description", "See other observers while spectating");
            config.addDefault("setting.Picker.values", Arrays.asList("off", "on[default]"));
            config.addDefault("setting.Picker.description", "Open a helpful GUI for picking classes and teams");
            config.addDefault("setting.PrivateMessages.aliases", Arrays.asList("pms"));
            config.addDefault("setting.PrivateMessages.values", Arrays.asList("none", "all[default]"));
            config.addDefault("setting.PrivateMessages.description", "Who can send you private messages");
            config.addDefault("setting.Scoreboard.values", Arrays.asList("off", "on[default]"));
            config.addDefault("setting.Scoreboard.description", "See the scoreboard with game information");
            config.addDefault("setting.Sounds.values", Arrays.asList("off", "on[default]"));
            config.addDefault("setting.Sounds.description", "Hear sounds to alert you of the last three seconds of a countdown");
        }
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
            gameHandler = new GameHandler(this);
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
