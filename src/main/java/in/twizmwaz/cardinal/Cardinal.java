package in.twizmwaz.cardinal;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocaleHandler;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.command.BroadcastCommands;
import in.twizmwaz.cardinal.command.CancelCommand;
import in.twizmwaz.cardinal.command.CardinalCommand;
import in.twizmwaz.cardinal.command.ChatCommands;
import in.twizmwaz.cardinal.command.ClassCommands;
import in.twizmwaz.cardinal.command.CycleCommand;
import in.twizmwaz.cardinal.command.InventoryCommand;
import in.twizmwaz.cardinal.command.JoinCommand;
import in.twizmwaz.cardinal.command.ListCommand;
import in.twizmwaz.cardinal.command.MapCommands;
import in.twizmwaz.cardinal.command.MatchCommand;
import in.twizmwaz.cardinal.command.ModesCommand;
import in.twizmwaz.cardinal.command.PrivateMessageCommands;
import in.twizmwaz.cardinal.command.ProximityCommand;
import in.twizmwaz.cardinal.command.PunishmentCommands;
import in.twizmwaz.cardinal.command.RankCommands;
import in.twizmwaz.cardinal.command.ReadyCommand;
import in.twizmwaz.cardinal.command.RotationCommands;
import in.twizmwaz.cardinal.command.ScoreCommand;
import in.twizmwaz.cardinal.command.SettingCommands;
import in.twizmwaz.cardinal.command.SnowflakesCommand;
import in.twizmwaz.cardinal.command.StartAndEndCommand;
import in.twizmwaz.cardinal.command.TeamCommands;
import in.twizmwaz.cardinal.command.TeleportCommands;
import in.twizmwaz.cardinal.command.TimeLimitCommand;
import in.twizmwaz.cardinal.command.WhitelistCommands;
import in.twizmwaz.cardinal.rank.Rank;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import in.twizmwaz.cardinal.settings.Setting;
import in.twizmwaz.cardinal.settings.SettingValue;
import in.twizmwaz.cardinal.tabList.TabList;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.DomUtil;
import in.twizmwaz.cardinal.util.Numbers;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cardinal extends JavaPlugin {

    private final static String CRAFTBUKKIT_VERSION = "v1_8_R3";
    private final static String MINECRAFT_VERSION = "1.8.7";

    private static Cardinal instance;
    private static GameHandler gameHandler;
    private static LocaleHandler localeHandler;
    private static Database database;
    private CommandsManager<CommandSender> commands;
    private File databaseFile;
    private TabList tabList;

    public static LocaleHandler getLocaleHandler() {
        return localeHandler;
    }

    public static Cardinal getInstance() {
        return instance;
    }

    public static Database getCardinalDatabase() {
        return database;
    }

    public TabList getTabList() {
        return tabList;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String locale = ChatUtil.getLocale(sender);
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
        cmdRegister.register(BroadcastCommands.class);
        cmdRegister.register(CancelCommand.class);
        cmdRegister.register(CardinalCommand.class);
        cmdRegister.register(ChatCommands.class);
        cmdRegister.register(ClassCommands.class);
        cmdRegister.register(CycleCommand.class);
        cmdRegister.register(InventoryCommand.class);
        cmdRegister.register(JoinCommand.class);
        cmdRegister.register(ListCommand.class);
        cmdRegister.register(MapCommands.class);
        cmdRegister.register(MatchCommand.class);
        cmdRegister.register(ModesCommand.class);
        cmdRegister.register(PrivateMessageCommands.class);
        cmdRegister.register(ProximityCommand.class);
        cmdRegister.register(PunishmentCommands.class);
        cmdRegister.register(RankCommands.RankParentCommand.class);
        cmdRegister.register(ReadyCommand.class);
        cmdRegister.register(RotationCommands.class);
        cmdRegister.register(ScoreCommand.class);
        cmdRegister.register(SettingCommands.class);
        cmdRegister.register(SnowflakesCommand.class);
        cmdRegister.register(StartAndEndCommand.class);
        cmdRegister.register(TeamCommands.TeamParentCommand.class);
        cmdRegister.register(TeamCommands.class);
        cmdRegister.register(TeleportCommands.class);
        cmdRegister.register(TimeLimitCommand.class);
        cmdRegister.register(WhitelistCommands.WhitelistParentCommand.class);


    }

    private void checkCraftVersion() {
        String craftVer = Bukkit.getServer().getClass().getPackage().getName();
        if (!("org.bukkit.craftbukkit." + CRAFTBUKKIT_VERSION).equals(craftVer)) {
            getLogger().warning("########################################");
            getLogger().warning("#####  YOUR VERSION OF SPORTBUKKIT #####");
            getLogger().warning("#####  IS NOT SUPPORTED. PLEASE    #####");
            getLogger().warning("#####  USE  SPORTBUKKIT " + MINECRAFT_VERSION + "      #####");
            getLogger().warning("########################################");
        }
    }

    private void deleteMatches() {
        FileConfiguration config = getConfig();
        if (config.getBoolean("deleteMatches")) {
            getLogger().info("Deleting match files, this can be disabled via the configuration");
            File matches = new File("matches/");
            try {
                FileUtils.deleteDirectory(matches);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerSettings() {
        FileConfiguration config = getConfig();
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
                if (config.contains("setting." + settingName + ".description"))
                    description = config.getString("setting." + settingName + ".description");
                if (config.contains("setting." + settingName + ".values")) {
                    for (String valueName : config.getStringList("setting." + settingName + ".values")) {
                        if (valueName.endsWith("[default]"))
                            values.add(new SettingValue(valueName.trim().substring(0, valueName.length() - 9), true));
                        else values.add(new SettingValue(valueName.trim(), false));
                    }
                }
                new Setting(names, description, values);
            }
        }
        new Setting(Arrays.asList("ChatChannel"), "Choose a default chat channel.", Arrays.asList(new SettingValue("global", false), new SettingValue("team", true), new SettingValue("admin", false)));
    }

    public void registerRanks() {
        File file = new File(getDataFolder(), "ranks.xml");
        if (!file.exists()) {
            try {
                FileUtils.copyInputStreamToFile(getResource("ranks.xml"), file);
            } catch (IOException e) {
                e.printStackTrace();
                getLogger().warning("Could not copy default ranks file to 'ranks.xml'.");
            }
        }
        try {
            Document document = DomUtil.parse(file);
            for (Element rank : document.getRootElement().getChildren("rank")) {
                String name = rank.getAttributeValue("name");
                boolean defaultRank = rank.getAttributeValue("default") != null && Numbers.parseBoolean(rank.getAttributeValue("default"));
                boolean staffRank = false;
                String parent = rank.getAttributeValue("parent");
                if (parent != null) {
                    for (Element element : document.getRootElement().getChildren("rank")) {
                        String elementName = element.getAttributeValue("name");
                        if (elementName.equalsIgnoreCase(parent) || elementName.toLowerCase().startsWith(parent.toLowerCase())) {
                            staffRank = element.getAttributeValue("staff") != null && Numbers.parseBoolean(element.getAttributeValue("staff"));
                        }
                    }
                    if (rank.getAttributeValue("staff") != null) {
                        staffRank = Numbers.parseBoolean(rank.getAttributeValue("staff"));
                    }
                } else {
                    staffRank = rank.getAttributeValue("staff") != null && Numbers.parseBoolean(rank.getAttributeValue("staff"));
                }
                String flair = rank.getAttributeValue("flair") != null ? ChatColor.translateAlternateColorCodes('`', rank.getAttributeValue("flair")) : "";
                List<String> permissions = new ArrayList<>(), disabledPermissions = new ArrayList<>();
                for (Element permission : rank.getChildren("permission")) {
                    if (permission.getText().startsWith("-")) {
                        disabledPermissions.add(permission.getText().substring(1));
                    } else {
                        permissions.add(permission.getText());
                    }
                }
                new Rank(name, defaultRank, staffRank, flair, permissions, disabledPermissions, parent);
            }

            for (Rank rank : Rank.getRanks()) {
                if (rank.getParent() != null &&
                        Rank.getRank(rank.getParent()) != null &&
                        Rank.getRank(rank.getParent()).getParent() != null &&
                        Rank.getRank(Rank.getRank(rank.getParent()).getParent()) != null &&
                        Rank.getRank(Rank.getRank(rank.getParent()).getParent()).equals(rank)) {
                    getLogger().warning("Rank inheritance processes were terminated because " + rank.getName() + " and " + Rank.getRank(rank.getParent()).getName() + " are parents of each other, which cannot occur.");
                    return;
                }
            }

            List<Rank> completed = new ArrayList<>();
            for (Rank rank : Rank.getRanks()) {
                if (rank.getParent() == null) {
                    completed.add(rank);
                }
            }
            while (!completed.containsAll(Rank.getRanks())) {
                Rank inheriting = null;
                for (Rank rank : Rank.getRanks()) {
                    if (!completed.contains(rank) && rank.getParent() != null && completed.contains(Rank.getRank(rank.getParent()))) {
                        inheriting = rank;
                    }
                }
                for (String permission : Rank.getRank(inheriting.getParent()).getPermissions()) {
                    inheriting.addPermission(permission);
                }
                    completed.add(inheriting);
            }
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
            getLogger().warning("Could not parse file 'ranks.xml' for ranks.");
        }

    }

    @Override
    public void onEnable() {
        instance = this;
        checkCraftVersion();
        try {
            localeHandler = new LocaleHandler(this);
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            getLogger().severe("Failed to initialize because of invalid language files. Please make sure all language files in the plugin are present.");
            setEnabled(false);
            return;
        }

        getDataFolder().mkdir();

        databaseFile = new File(getDataFolder(), "database.xml");
        if (databaseFile.exists()) {
            try {
                database = Database.loadFromFile(databaseFile);
            } catch (JDOMException | IOException e) {
                e.printStackTrace();
                getLogger().severe("Failed to initialize because of an IOException. Please try restarting your server.");
                setEnabled(false);
                return;
            }
        } else {
            database = Database.newInstance(databaseFile);
        }

        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();

        deleteMatches();
        registerSettings();
        registerRanks();
        setupCommands();
        this.tabList = new TabList();
        Bukkit.getPluginManager().registerEvents(tabList, this);

        try {
            gameHandler = new GameHandler();
        } catch (RotationLoadException e) {
            e.printStackTrace();
            getLogger().severe("Failed to initialize because of an invalid rotation configuration.");
            setEnabled(false);
            return;
        }

        if (config.getBoolean("resetSpawnProtection") && Bukkit.getServer().getSpawnRadius() != 0) {
            Bukkit.getServer().setSpawnRadius(0);
        }
    }

    @Override
    public void onDisable() {
        database.save(databaseFile);
        saveConfig();
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public JavaPlugin getPlugin() {
        return this;
    }
}
