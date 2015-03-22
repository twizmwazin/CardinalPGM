package in.twizmwaz.cardinal.module.modules.stats;

import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class Stats implements Module {


    private List<MatchTracker> stats;
    private int totalKills;
    private int totalDeaths;

    protected Stats() {
        stats = new ArrayList<>();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public void add(MatchTracker tracker) {
        stats.add(tracker);
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        this.add(new MatchTracker(event.getPlayer(), event.getKiller(), event.getPlayer().getItemInHand()));
    }

 /* private Material getMostCommonWeapon() {
        Material item = null;
        int occurences = 0;
        HashMap<Material, Integer> itemOccurences = new HashMap<>();
        for (MatchTracker tracker : this.stats) {
            itemOccurences.put(tracker.getWeapon().getType(), itemOccurences.containsKey(tracker.getWeapon().getType()) ? itemOccurences.get(tracker.getWeapon().getType()) + 1 : 1);
        }
        for (Material material : itemOccurences.keySet()) {
            if (itemOccurences.get(material) > occurences) {
                item = material;
                occurences = itemOccurences.get(material);
            }
        }
        return item;
    }

    private OfflinePlayer getPlayerWithMostKillsOnTeam(TeamModule team) {
        OfflinePlayer player = null;
        int occurences = 0;
        HashMap<OfflinePlayer, Integer> playerOccurences = new HashMap<>();
        for (MatchTracker tracker : this.stats) {
            if (tracker.getKiller() != null) {
                playerOccurences.put(tracker.getKiller(), playerOccurences.containsKey(tracker.getKiller()) ? playerOccurences.get(tracker.getKiller()) + 1 : 1);
            }
        }
        for (OfflinePlayer offlinePlayer : playerOccurences.keySet()) {
            if (playerOccurences.get(offlinePlayer) > occurences) {
                player = offlinePlayer;
                occurences = playerOccurences.get(offlinePlayer);
            }
        }
        return player;
    }

    private OfflinePlayer getPlayerWithMostDeathsOnTeam(TeamModule team) {
        OfflinePlayer player = null;
        int occurences = 0;
        HashMap<OfflinePlayer, Integer> playerOccurences = new HashMap<>();
        for (MatchTracker tracker : this.stats) {
            playerOccurences.put(tracker.getPlayer(), playerOccurences.containsKey(tracker.getPlayer()) ? playerOccurences.get(tracker.getPlayer()) + 1 : 1);
        }
        for (OfflinePlayer offlinePlayer : playerOccurences.keySet()) {
            if (playerOccurences.get(offlinePlayer) > occurences) {
                player = offlinePlayer;
                occurences = playerOccurences.get(offlinePlayer);
            }
        }
        return player;
    } */

    public int getKillsByPlayer(OfflinePlayer player) {
        int kills = 0;
        if (player == null) return 0;
        for (MatchTracker tracker : this.stats) {
            if (tracker.getKiller() != null && tracker.getKiller().equals(player)) {
                kills ++;
            }
        }
        return kills;
    }

    public int getDeathsByPlayer(OfflinePlayer player) {
        int deaths = 0;
        if (player == null) return 0;
        for (MatchTracker tracker : this.stats) {
            if (tracker.getPlayer().equals(player)) {
                deaths ++;
            }
        }
        return deaths;
    }

    public double getKdByPlayer(OfflinePlayer player) {
        double kd;
        if (player == null) return 0;
        kd = getDeathsByPlayer(player) == 0 ? (double) getKillsByPlayer(player) : (double) getKillsByPlayer(player) / (double) getDeathsByPlayer(player);
        return kd;
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Settings.getSettingByName("Stats") != null && Settings.getSettingByName("Stats").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                player.sendMessage(ChatColor.GRAY + "Kills: " + ChatColor.GREEN + getKillsByPlayer(player) + ChatColor.AQUA + " | " + ChatColor.GRAY + "Deaths: " + ChatColor.DARK_RED + getDeathsByPlayer(player) + ChatColor.AQUA + " | " + ChatColor.GRAY + "KD: " + ChatColor.GOLD + String.format("%.2f", getKdByPlayer(player)));
            }
        }
    }
}
