package in.twizmwaz.cardinal.module.modules.classModule;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.event.ClassChangeEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.UUID;

public class ClassModule implements Module {

    public static HashMap<UUID, ClassModule> playerClass = new HashMap<>();

    private final String name;
    private final String description;
    private final String longDescription;
    private final Material icon;
    private final boolean sticky;
    private final boolean defaultClass;
    private final boolean restrict;

    private final Kit kit;

    protected ClassModule(final String name, final String description, final String longDescription, final Material icon, final boolean sticky, final boolean defaultClass, final boolean restrict, final Kit kit) {
        this.name = name;
        this.description = description;
        this.longDescription = longDescription;
        this.icon = icon;
        this.sticky = sticky;
        this.defaultClass = defaultClass;
        this.restrict = restrict;

        this.kit = kit;
    }

    @EventHandler
    public void onClassChange(ClassChangeEvent event) {
        if (event.getClassModule().equals(this)) {
            if (sticky && TeamUtils.getTeamByPlayer(event.getPlayer()) != null && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NO_CLASS_CHANGE).getMessage(event.getPlayer().getLocale()));
            }
            if (!restrict && !event.getPlayer().isOp()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_CLASS_RESTRICTED, ChatColor.AQUA + name + ChatColor.RED).getMessage(event.getPlayer().getLocale()));
            }
            if (!event.isCancelled()) {
                event.getPlayer().sendMessage(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.GENERIC_HAVE_SELECTED, ChatColor.GOLD + "" + ChatColor.UNDERLINE + name + ChatColor.GREEN).getMessage(event.getPlayer().getLocale()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPgmSpawn(CardinalSpawnEvent event) {
        if (!playerClass.containsKey(event.getPlayer().getUniqueId()) && (this.defaultClass || (!defaultClassPresent() && GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class).equals(this)))) playerClass.put(event.getPlayer().getUniqueId(), this);
        if (playerClass.containsKey(event.getPlayer().getUniqueId()) && playerClass.get(event.getPlayer().getUniqueId()).equals(this)) {
            if (kit != null) kit.apply(event.getPlayer());
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public Material getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public boolean isDefaultClass() {
        return defaultClass;
    }

    public static ClassModule getClassByPlayer(Player player) {
        if (playerClass.containsKey(player.getUniqueId())) return playerClass.get(player.getUniqueId());
        return null;
    }

    public static ClassModule getClassByName(String name) {
        for (ClassModule classModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ClassModule.class)) {
            if (classModule.getName().equalsIgnoreCase(name) || classModule.getName().toLowerCase().startsWith(name.toLowerCase())) return classModule;
        }
        return null;
    }

    public static boolean defaultClassPresent() {
        for (ClassModule classModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ClassModule.class)) {
            if (classModule.isDefaultClass()) return true;
        }
        return false;
    }

    public String getDescription() {
        return description;
    }
}
