package in.twizmwaz.cardinal.module.modules.classModule;

import in.twizmwaz.cardinal.event.ClassChangeEvent;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    protected ClassModule(final String name, final String description, final String longDescription, final Material icon, final boolean sticky, final boolean defaultClass, final boolean restrict) {
        this.name = name;
        this.description = description;
        this.longDescription = longDescription;
        this.icon = icon;
        this.sticky = sticky;
        this.defaultClass = defaultClass;
        this.restrict = restrict;
    }

    @EventHandler
    public void onClassChange(ClassChangeEvent event) {
        if (event.getClassModule().isSticky() && TeamUtils.getTeamByPlayer(event.getPlayer()) != null && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver()) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), "You may not change your class during the match.");
        }
    }

    @EventHandler
    public void onPgmSpawn(PgmSpawnEvent event) {
        if (!playerClass.containsKey(event.getPlayer().getUniqueId()) && this.defaultClass) playerClass.put(event.getPlayer().getUniqueId(), this);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public boolean isSticky() {
        return sticky;
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

    public static ClassModule getClassByPlayer(Player player) {
        if (playerClass.containsKey(player.getUniqueId())) return playerClass.get(player.getUniqueId());
        return null;
    }
}
