package in.twizmwaz.cardinal.module.modules.permissions;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.MojangUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PermissionModule implements Module {

    private final Plugin plugin;
    private final Map<Player, PermissionAttachment> attachmentMap;

    private List<UUID> devs = Arrays.asList(UUID.fromString("670223bb-7560-48c8-8f01-2f463549b917") /* twiz_mwazin */, UUID.fromString("33a703d0-3237-4337-9ddd-3dbf33b3d8a6") /* iEli2tyree011 */, UUID.fromString("208c84af-790a-41da-bf7e-eb184f17bdf8") /* Elly */, UUID.fromString("260004f0-996b-4539-ba21-df4ee6336b63") /* Elliott_ */);

    public PermissionModule(Plugin plugin) {
        this.plugin = plugin;
        this.attachmentMap = new HashMap<>();
    }

    @Override
    public void unload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.removeAttachment(attachmentMap.get(player));
        }

        attachmentMap.clear();
    }

    public List<UUID> getDevs() {
        return devs;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        attachmentMap.put(event.getPlayer(), event.getPlayer().addAttachment(plugin));
        for (String permisson : GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Default.permissions")) {
            attachmentMap.get(event.getPlayer()).setPermission(permisson, true);
        }
        if (event.getPlayer().isOp() && GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Moderator.players").contains(event.getPlayer().getUniqueId().toString())) {
            List<String> players = new ArrayList<>();
            players.addAll(GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Moderator.players"));
            players.remove(event.getPlayer().getUniqueId().toString());
            GameHandler.getGameHandler().getPlugin().getConfig().set("permissions.Moderator.players", players);
            GameHandler.getGameHandler().getPlugin().saveConfig();
        }
        if (GameHandler.getGameHandler().getPlugin().getConfig().get("permissions.Moderator.players") != null) {
            for (String uuid : GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Moderator.players")) {
                if (event.getPlayer().getUniqueId().toString().equals(uuid)) {
                    for (String permission : GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Moderator.permissions")) {
                        attachmentMap.get(event.getPlayer()).setPermission(permission, true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers())
            attachmentMap.put(player, player.addAttachment(plugin));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (attachmentMap.get(event.getPlayer()) != null) {
            event.getPlayer().removeAttachment(attachmentMap.get(event.getPlayer()));
            attachmentMap.remove(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        event.getPlayer().removeAttachment(attachmentMap.get(event.getPlayer()));
        attachmentMap.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (Cardinal.getInstance().getConfig().getBoolean("worldEditPermissions"))
        if (event.getNewTeam().isObserver()) {
            attachmentMap.get(event.getPlayer()).setPermission("worldedit.navigation.jumpto.tool", true);
            attachmentMap.get(event.getPlayer()).setPermission("worldedit.navigation.thru.tool", true);

            attachmentMap.get(event.getPlayer()).setPermission("worldedit.navigation.jumpto.command", true);
            attachmentMap.get(event.getPlayer()).setPermission("worldedit.navigation.thru.command", true);
        } else {
            attachmentMap.get(event.getPlayer()).setPermission("worldedit.navigation.jumpto.tool", false);
            attachmentMap.get(event.getPlayer()).setPermission("worldedit.navigation.thru.tool", false);

            attachmentMap.get(event.getPlayer()).setPermission("worldedit.navigation.jumpto.command", false);
            attachmentMap.get(event.getPlayer()).setPermission("worldedit.navigation.thru.command", false);
        }
    }

    public PermissionAttachment getPlayerAttachment(Player player) {
        return attachmentMap.get(player);
    }

    public static boolean isMod(UUID player) {
        for (String uuid : GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.moderator.players")) {
            if (uuid.equals(player.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDev(UUID player) {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).getDevs().contains(player);
    }

    @EventHandler
    public void onPlayerNameUpdate(PlayerNameUpdateEvent event) {
        String star = "\u2756";
        String stars = "";
        if (event.getPlayer().isOp()) {
            stars += ChatColor.GOLD + star;
        } else if (isMod(event.getPlayer().getUniqueId())) {
            stars += ChatColor.RED + star;
        }
        if (devs.contains(event.getPlayer().getUniqueId())) {
            stars += ChatColor.DARK_PURPLE + star;
        }
        event.getPlayer().setDisplayName(stars + event.getTeam().getColor() + event.getPlayer().getName());
        event.getPlayer().setPlayerListName(stars + event.getTeam().getColor() + event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeTeam2(PlayerChangeTeamEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(new PlayerNameUpdateEvent(event.getPlayer(), event.getNewTeam()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin2(PlayerJoinEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(new PlayerNameUpdateEvent(event.getPlayer()));
    }

    public void disablePermission(Player player, String permission) {
        attachmentMap.get(player).unsetPermission(permission);
    }

    public void enablePermission(Player player, String permission) {
        attachmentMap.get(player).setPermission(permission, true);
    }
    
}
