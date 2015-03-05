package in.twizmwaz.cardinal.module.modules.stats;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class Stats implements Module {


    private List<Tracker> stats = new ArrayList<>();
    private int totalKills;
    private int totalDeaths;

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public void add(Tracker tracker) {
        stats.add(tracker);
    }

    private Material getMostCommonWeapon() {
        Map<Material, Integer> count = new HashMap<>();
        for (Tracker tracker : stats) {
            if (tracker.getWeapon() != null) {
                Integer i = count.get(tracker.getWeapon().getType());
                if (i == null) i = 0;
                i++;
                count.put(tracker.getWeapon().getType(), i);
            }
        }
        Map.Entry<Material, Integer> mostRepeted = null;
        for (Map.Entry<Material, Integer> e : count.entrySet()) {
            if (mostRepeted == null || mostRepeted.getValue() < e.getValue()) {
                mostRepeted = e;
            }
        }
        return mostRepeted != null ? mostRepeted.getKey() : null;
    }

    private Player getPlayerWithMostKillsOnTeam(TeamModule team) {
        Map<Player, Integer> count = new HashMap<>();
        for (Tracker tracker : stats) {
            if (TeamUtils.getTeamByPlayer(tracker.getDamager()).equals(team)) {
                Integer i = count.get(tracker.getDamager());
                if (i == null) i = 0;
                i++;
                count.put(tracker.getDamager(), i);
            }
        }
        Map.Entry<Player, Integer> mostRepeted = null;
        for (Map.Entry<Player, Integer> e : count.entrySet()) {
            if (mostRepeted == null || mostRepeted.getValue() < e.getValue()) {
                mostRepeted = e;
            }
        }
        return mostRepeted != null ? mostRepeted.getKey() : null;
    }

    private Player getPlayerWithMostDeathsOnTeam(TeamModule team) {
        Map<Player, Integer> count = new HashMap<>();
        for (Tracker tracker : stats) {
            if (TeamUtils.getTeamByPlayer(tracker.getDamaged()).equals(team)) {
                Integer i = count.get(tracker.getDamaged());
                if (i == null) i = 0;
                i++;
                count.put(tracker.getDamaged(), i);
            }
        }
        Map.Entry<Player, Integer> mostRepeted = null;
        for (Map.Entry<Player, Integer> e : count.entrySet()) {
            if (mostRepeted == null || mostRepeted.getValue() < e.getValue()) {
                mostRepeted = e;
            }
        }
        return mostRepeted != null ? mostRepeted.getKey() : null;
    }

    private int getKillsByPlayer(Player player) {
        int kills = 0;
        for (Tracker tracker : stats) {
            if (tracker.getDamager().equals(player)) kills++;
        }
        return kills;
    }

    private int getDeathsByPlayer(Player player) {
        int deaths = 0;
        for (Tracker tracker : stats) {
            if (tracker.getDamaged().equals(player)) deaths++;
        }
        return deaths;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null && event.getEntity().getKiller() != null) {
            add(new Tracker(event.getEntity(), event.getEntity().getKiller(), event.getEntity().getKiller().getItemInHand() != null ? event.getEntity().getKiller().getItemInHand() : null));
        }
    }

    @EventHandler
    public void matchEndEvent(MatchEndEvent event) {
        if (GameHandler.getGameHandler().getPlugin().getConfig().getBoolean("broadcastStats")) {
            for (Tracker tracker : stats) {
                totalDeaths++;
                totalKills++;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "---------------" + ChatColor.AQUA + " Stats " + ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "---------------");
                player.sendMessage("              " + ChatColor.GREEN + "Kills: " + totalKills + "              " + ChatColor.RED + "Deaths: " + totalDeaths);
                for (TeamModule team : TeamUtils.getTeams()) {
                    player.sendMessage("");
                    if (team != null && !team.isObserver()) {
                        int teamKills = 0;
                        int teamDeaths = 0;
                        for (Tracker tracker : stats) {
                            if (TeamUtils.getTeamByPlayer(tracker.getDamager()).equals(team)) teamKills++;
                            if (TeamUtils.getTeamByPlayer(tracker.getDamaged()).equals(team)) teamDeaths++;
                        }
                        if (getPlayerWithMostKillsOnTeam(team) != null && getPlayerWithMostDeathsOnTeam(team) != null) {
                            player.sendMessage("    " + team.getColor() + ChatColor.BOLD + team.getCompleteName() + ChatColor.GREEN + "    kills: " + ChatColor.WHITE + teamKills + ChatColor.RED + "    deaths     " + ChatColor.WHITE + teamDeaths + ChatColor.GOLD + " KD " + String.format("%.2f", (double) teamKills / ((double) teamDeaths > 0 ? (double) teamDeaths : 1.0)));
                            player.sendMessage("");
                            player.sendMessage("        " + ChatColor.GREEN + "Most Kills " + getPlayerWithMostKillsOnTeam(team).getDisplayName() + ": " + getKillsByPlayer(getPlayerWithMostKillsOnTeam(team)));
                            player.sendMessage("        " + ChatColor.RED + "Most Deaths " + getPlayerWithMostDeathsOnTeam(team).getDisplayName() + ": " + getDeathsByPlayer(getPlayerWithMostDeathsOnTeam(team)));
                            player.sendMessage("");
                        } else {
                            player.sendMessage(ChatColor.DARK_RED + "No Team Statistics Available for " + team.getCompleteName());
                        }
                    }
                }
                player.sendMessage("");
                if (getMostCommonWeapon() != null) player.sendMessage(ChatColor.GOLD + "Most Common Weapon: " + ChatColor.WHITE + (getMostCommonWeapon() != null ? getMostCommonWeapon().toString().toLowerCase().replace('_', ' ') : "None"));
                player.sendMessage("              " + ChatColor.AQUA + "Your stats              ");
                player.sendMessage(ChatColor.GREEN + "        Kills: " + getKillsByPlayer(player));
                player.sendMessage(ChatColor.RED + "        Deaths: " + getDeathsByPlayer(player));
                player.sendMessage(ChatColor.GOLD + "        KD: " +  String.format("%.2f", (double) getKillsByPlayer(player) / ((double) getDeathsByPlayer(player) > 0 ? (double) getDeathsByPlayer(player) : 1)));
            }
        }
    }
}
