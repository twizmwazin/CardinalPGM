package in.twizmwaz.cardinal.rank;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.RankChangeEvent;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.util.Contributor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class Rank {

    private static List<Rank> ranks = new ArrayList<>();
    private String name;
    private String flair;
    private List<String> disabledPermissions;
    private String parent;
    private boolean staffRank, defaultRank;
    private List<String> permissions;

    public Rank(String name, boolean defaultRank, boolean staffRank, String flair, List<String> permissions, List<String> disabledPermissions, String parent) {
        this.name = name;
        this.defaultRank = defaultRank;
        this.staffRank = staffRank;
        this.flair = flair;
        this.permissions = permissions;
        this.disabledPermissions = disabledPermissions;
        this.parent = parent;

        ranks.add(this);
    }

    public static void clearRanks() {
        ranks = new ArrayList<>();
    }

    public static List<Rank> getRanks() {
        return ranks;
    }

    public static List<Rank> getDefaultRanks() {
        List<Rank> results = new ArrayList<>();
        for (Rank rank : ranks) {
            if (rank.isDefaultRank()) {
                results.add(rank);
            }
        }
        return results;
    }

    public static List<Rank> getRanks(UUID uuid) {
        List<Rank> ranks = new ArrayList<>();
        for (Rank rank : Rank.ranks) {
            if (rank.contains(uuid)) {
                ranks.add(rank);
            }
        }
        return ranks;
    }

    public static Rank getRank(String name) {
        for (Rank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        for (Rank rank : ranks) {
            if (rank.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return rank;
            }
        }
        return null;
    }

    public static boolean isMapAuthor(UUID uuid) {
        if (!Bukkit.getOfflinePlayer(uuid).isOnline()) return false;
        for (Contributor author : GameHandler.getGameHandler().getMatch().getLoadedMap().getAuthors()) {
            if (author.getUniqueId() != null && author.getUniqueId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public static boolean whitelistBypass(UUID uuid) {
        for (Rank rank : getRanks(uuid)) {
            if (rank.getPermissions().contains("cardinal.whitelist.bypass")) return true;
        }
        return false;
    }

    public static String getPrefix(UUID uuid) {
        String prefix = "";
        String staffChar = "\u2756";
        if (Bukkit.getOfflinePlayer(uuid).isOp()) {
            prefix += ChatColor.GOLD + staffChar;
        }
        if (PermissionModule.isDeveloper(uuid)) {
            prefix += ChatColor.DARK_PURPLE + staffChar;
        }
        for (Rank rank : getRanks(uuid)) {
            if (!rank.staffRank) continue;
            if (rank.contains(uuid)) {
                prefix += rank.getFlair();
            }
        }
        if (isMapAuthor(uuid)) {
            prefix += ChatColor.BLUE + "*";
        }
        for (Rank rank : getRanks(uuid)) {
            if (rank.staffRank) continue;
            if (rank.contains(uuid)) {
                prefix += rank.getFlair();
            }
        }
        return prefix;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event, UUID uuid) {
        if (PermissionModule.isDeveloper(uuid)){
            String lamamadezenzoh = ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "------" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------" + ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "------";
            Bukkit.broadcastMessage(lamamadezenzoh);
            Bukkit.broadcastMessage(ChatColor.RED + "A developer joined the game.");
            Bukkit.broadcastMessage(lamamadezenzoh);
        }
    }

    public String getName() {
        return name;
    }

    public String getFlair() {
        return flair;
    }

    public boolean isStaffRank() {
        return staffRank;
    }

    public boolean isDefaultRank() {
        return defaultRank;
    }

    public void add(UUID uuid) {
        Cardinal.getCardinalDatabase().put(this, uuid);
        Bukkit.getPluginManager().callEvent(new RankChangeEvent(Bukkit.getPlayer(uuid), this, true, Bukkit.getOfflinePlayer(uuid).isOnline()));
    }

    public void remove(UUID uuid) {
        Cardinal.getCardinalDatabase().remove(this, uuid);
        Bukkit.getPluginManager().callEvent(new RankChangeEvent(Bukkit.getPlayer(uuid), this, false, Bukkit.getOfflinePlayer(uuid).isOnline()));
    }

    public boolean contains(UUID uuid) {
        return Cardinal.getCardinalDatabase().get(this, uuid);
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public String getParent() {
        return parent;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }

    public List<String> getDisabledPermissions() {
        return disabledPermissions;
    }
}
