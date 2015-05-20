package in.twizmwaz.cardinal.rank;

import in.twizmwaz.cardinal.event.RankChangeEvent;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Rank {

    private String name;
    private String flair;
    private boolean staffRank;
    private boolean defaultRank;

    private static List<Rank> ranks = new ArrayList<>();
    private static HashMap<UUID, List<Rank>> playerRanks = new HashMap<>();

    public Rank(String name, String flair, boolean staffRank, boolean defaultRank) {
        this.name = name;
        this.flair = flair;
        this.staffRank = staffRank;
        this.defaultRank = defaultRank;

        ranks.add(this);
    }

    public static List<Rank> getRanks() {
        return ranks;
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

    public void addPlayer(UUID player) {
        if (!playerRanks.containsKey(player)) {
            playerRanks.put(player, new ArrayList<Rank>());
        }
        playerRanks.get(player).add(this);
    }

    public void removePlayer(UUID player) {
        if (!playerRanks.containsKey(player)) {
            playerRanks.put(player, new ArrayList<Rank>());
        }
        playerRanks.get(player).remove(this);
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

    public static List<Rank> getRanksByPlayer(UUID player) {
        return playerRanks.containsKey(player) ? playerRanks.get(player) : new ArrayList<Rank>();
    }

    public static String getPlayerPrefix(UUID player) {
        String prefix = "";
        String staffChar = "\u2756";
        if (Bukkit.getOfflinePlayer(player).isOp()) {
            prefix += ChatColor.GOLD + staffChar;
        } else if (PermissionModule.isMod(player)) {
            prefix += ChatColor.RED + staffChar;
        }
        if (PermissionModule.isDeveloper(player)) {
            prefix += ChatColor.DARK_PURPLE + staffChar;
        }
        return prefix;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Rank rank : getDefaultRanks()) {
            rank.addPlayer(event.getPlayer().getUniqueId());
        }
//        Bukkit.getPluginManager().callEvent(new RankChangeEvent());
    }

}
