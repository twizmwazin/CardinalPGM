package in.twizmwaz.cardinal.module.modules.permissions;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.*;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.TeamUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
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

    private List<OfflinePlayer> muted = new ArrayList<>();

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
        for (String permission : GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions." + getDefaultRank() + ".permissions")) {
            attachmentMap.get(event.getPlayer()).setPermission(permission, true);
        }

        for (String rank : getRanks(event.getPlayer().getUniqueId())) {
            for (String permission : Cardinal.getInstance().getConfig().getStringList("permissions." + rank + ".permissions")) {
                if (permission.startsWith("-")) {
                    GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).attachmentMap.get(event.getPlayer()).setPermission(permission.substring(1), true);
                } else {
                    GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).attachmentMap.get(event.getPlayer()).setPermission(permission, false);
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
        if (Cardinal.getInstance().getConfig().getBoolean("worldEditPermissions")) {
            if (event.getNewTeam().isObserver() || !GameHandler.getGameHandler().getMatch().isRunning()) {
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
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        if (Cardinal.getInstance().getConfig().getBoolean("worldEditPermissions")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (TeamUtils.getTeamByPlayer(player) == null || TeamUtils.getTeamByPlayer(player).isObserver()) {
                    attachmentMap.get(player).setPermission("worldedit.navigation.jumpto.tool", true);
                    attachmentMap.get(player).setPermission("worldedit.navigation.thru.tool", true);

                    attachmentMap.get(player).setPermission("worldedit.navigation.jumpto.command", true);
                    attachmentMap.get(player).setPermission("worldedit.navigation.thru.command", true);
                } else {
                    attachmentMap.get(player).setPermission("worldedit.navigation.jumpto.tool", false);
                    attachmentMap.get(player).setPermission("worldedit.navigation.thru.tool", false);

                    attachmentMap.get(player).setPermission("worldedit.navigation.jumpto.command", false);
                    attachmentMap.get(player).setPermission("worldedit.navigation.thru.command", false);
                }
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        if (Cardinal.getInstance().getConfig().getBoolean("worldEditPermissions")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                attachmentMap.get(player).setPermission("worldedit.navigation.jumpto.tool", true);
                attachmentMap.get(player).setPermission("worldedit.navigation.thru.tool", true);

                attachmentMap.get(player).setPermission("worldedit.navigation.jumpto.command", true);
                attachmentMap.get(player).setPermission("worldedit.navigation.thru.command", true);
            }
        }
    }

    public PermissionAttachment getPlayerAttachment(Player player) {
        return attachmentMap.get(player);
    }

    public static boolean isMod(UUID player) {
        return hasRank(player, "Moderator");
    }

    public static boolean isStaff(OfflinePlayer player) {
        if (player.isOp()) {
            return true;
        }
        for (String rank : getRanks(player.getUniqueId())) {
            if (isStaffRank(rank)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDev(UUID player) {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).getDevs().contains(player);
    }

    public static boolean hasRank(UUID player, String rank) {
        if (!rankExists(rank)) {
            return false;
        }
        return Cardinal.getInstance().getConfig().getStringList("permissions." + rank + ".players").contains(player.toString());
    }

    public static boolean rankExists(String rank) {
        return Cardinal.getInstance().getConfig().getStringList("permissions." + rank + ".permissions") != null;
    }

    public static void giveRank(UUID player, String rank) {
        if (!rankExists(rank)) {
            return;
        }
        List<String> players = Cardinal.getInstance().getConfig().getStringList("permissions." + rank + ".players"); 
        players.add(player.toString());
        Cardinal.getInstance().getConfig().set("permissions." + rank + ".players", players);
        for (String permission : Cardinal.getInstance().getConfig().getStringList("permissions." + rank + ".permissions")) {
            if (permission.startsWith("-")) {
                GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).attachmentMap.get(Bukkit.getPlayer(player)).setPermission(permission.substring(1), false);
            } else {
                GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).attachmentMap.get(Bukkit.getPlayer(player)).setPermission(permission, true);
            }
        }
        Bukkit.getPlayer(player).sendMessage(ChatColor.YELLOW + "You were given " + rank + "!");
        Cardinal.getInstance().saveConfig();
    }

    public static void takeRank(UUID player, String rank) {
        if (!rankExists(rank)) {
            return;
        }
        List<String> players = Cardinal.getInstance().getConfig().getStringList("permissions." + rank + ".players"); 
        players.remove(player.toString());
        Cardinal.getInstance().getConfig().set("permissions." + rank + ".players", players);
        for (String permission : Cardinal.getInstance().getConfig().getStringList("permissions." + rank + ".permissions")) {
            if (permission.startsWith("-")) {
                GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).attachmentMap.get(Bukkit.getPlayer(player)).setPermission(permission.substring(1), true);
            } else {
                GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).attachmentMap.get(Bukkit.getPlayer(player)).setPermission(permission, false);
            }
        }
        Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Your " + rank + " rank was removed!");
        Cardinal.getInstance().saveConfig();
    }

    public static List<String> getRanks(UUID player) {
        List<String> ranks = new ArrayList<String>();
        for (String rank : listRanks()) {
            if (Cardinal.getInstance().getConfig().getStringList("permissions." + rank + ".players").contains(player.toString())) {
                ranks.add(rank);
            }
        }
        return ranks;
    }

    public static List<String> listRanks() {
        List<String> ranks = new ArrayList<String>();
        ConfigurationSection perms = Cardinal.getInstance().getConfig().getConfigurationSection("permissions");
        for (String rank : perms.getKeys(false)) {
            ranks.add(rank);
        }
        return ranks;
    }

    public static boolean isStaffRank(String rank) {
        return Cardinal.getInstance().getConfig().getBoolean("permissions." + rank + ".staff");
    }

    public String getDefaultRank() {
        String foundRank = "";
        boolean found = false;
        for (String rank : listRanks()) {
            if (Cardinal.getInstance().getConfig().getBoolean("permissions." + rank + ".default")) {
                if (found) {
                    throw new IllegalArgumentException("There cannot be two default ranks!");
                }

                found = true;
                foundRank = rank;
            }
        }
        return foundRank;
    }

    @EventHandler
    public void onPlayerNameUpdate(PlayerNameUpdateEvent event) {
        String star = "\u2756";
        String stars = "";
        if (event.getPlayer().isOp()) {
            stars += ChatColor.GOLD + star;
        }

        for (String rank : getRanks(event.getPlayer().getUniqueId())) {
            stars += ChatColor.translateAlternateColorCodes('&', Cardinal.getInstance().getConfig().getString("permissions." + rank + ".prefix").replace("(star)", star));
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

    public void mute(Player player) {
        if (!muted.contains(player)) {
            muted.add(player);
            disablePermission(player, "cardinal.chat.team");
            disablePermission(player, "cardinal.chat.global");
        }
    }

    public void unmute(Player player) {
        if (muted.contains(player)) {
            muted.remove(player);
            enablePermission(player, "cardinal.chat.team");
            enablePermission(player, "cardinal.chat.global");
        }
    }

    public boolean isMuted(Player player) {
        return muted.contains(player);
    }

}
