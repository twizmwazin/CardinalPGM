package in.twizmwaz.cardinal.module.modules.respawn;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.PlayerUtil;
import in.twizmwaz.cardinal.util.TeamUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInitialSpawnEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public class RespawnModule implements Module {

    private final JavaPlugin plugin;
    private final Match match;

    protected RespawnModule(Match match) {
        this.plugin = GameHandler.getGameHandler().getPlugin();
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPgmSpawn(PgmSpawnEvent event) {
        try {
            if (!TeamUtil.getTeamByPlayer(event.getPlayer()).isObserver()) {
                event.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
            event.getPlayer().updateInventory();
        } catch (NullPointerException e) {
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInitLogin(PlayerInitialSpawnEvent event) {
        Bukkit.getLogger().log(Level.INFO, TeamUtil.getTeamById("observers").getName());
        TeamModule teamModule = TeamUtil.getTeamById("observers");
        SpawnModule spawn = null;
        for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
            if (spawnModule.getTeam().equals(teamModule)) {
                spawn = spawnModule;
                Bukkit.getLogger().log(Level.INFO, "yep");
            } else Bukkit.getLogger().log(Level.INFO, "nop");
        }
        PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn, TeamUtil.getTeamById("observers"));
        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
        if (!spawnEvent.isCancelled()) {
            event.setSpawnLocation(spawn.getLocation());
            PlayerUtil.resetPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onMinecraftRespawn(PlayerRespawnEvent event) {
        TeamModule teamModule = TeamUtil.getTeamByPlayer(event.getPlayer());
        SpawnModule spawn = null;
        for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
            if (spawnModule.getTeam().equals(teamModule)) spawn = spawnModule;
        }
        PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn, TeamUtil.getTeamByPlayer(event.getPlayer()));
        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
        if (!spawnEvent.isCancelled()) {
            event.setRespawnLocation(spawn.getLocation());
        }
    }


    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!TeamUtil.getTeamByPlayer(player).isObserver()) {
                PlayerUtil.resetPlayer(player);
                TeamModule teamModule = TeamUtil.getTeamByPlayer(player);
                SpawnModule spawn = null;
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam().equals(teamModule)) spawn = spawnModule;
                }
                PgmSpawnEvent spawnEvent = new PgmSpawnEvent(player, spawn, TeamUtil.getTeamByPlayer(player));
                Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                if (!spawnEvent.isCancelled()) {
                    player.teleport(spawn.getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            TeamModule teamModule = TeamUtil.getTeamByPlayer(player);
            SpawnModule spawn = null;
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam().equals(teamModule)) spawn = spawnModule;
            }
            PgmSpawnEvent spawnEvent = new PgmSpawnEvent(player, spawn, TeamUtil.getTeamById("observers"));
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                PlayerUtil.resetPlayer(player);
                player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
                ItemStack howTo = new ItemStack(Material.WRITTEN_BOOK);
                ItemMeta howToMeta = howTo.getItemMeta();
                howToMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Coming Soon");
                howTo.setItemMeta(howToMeta);
                BookMeta howToBookMeta = (BookMeta) howTo.getItemMeta();
                howToBookMeta.setAuthor(ChatColor.GOLD + "CardinalPGM");
                howTo.setItemMeta(howToBookMeta);
                player.getInventory().setItem(1, howTo);
                if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
                    ItemStack picker = new ItemStack(Material.LEATHER_HELMET);
                    ItemMeta pickerMeta = picker.getItemMeta();
                    pickerMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Team Selection");
                    pickerMeta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Join the game!"));
                    picker.setItemMeta(pickerMeta);
                    player.getInventory().setItem(2, picker);
                }
                player.teleport(spawn.getLocation());
            }
        }
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (match.getState().equals(MatchState.ENDED)) {
            event.setCancelled(true);
        }
        if (match.getState().equals(MatchState.PLAYING)) {
            try {
                if (!event.getNewTeam().isObserver()) {
                    if (event.getOldTeam().isObserver()) {
                        PlayerUtil.resetPlayer(event.getPlayer());
                        TeamModule teamModule = event.getNewTeam();
                        SpawnModule spawn = null;
                        for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                            if (spawnModule.getTeam().equals(teamModule)) spawn = spawnModule;
                        }
                        PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn, event.getNewTeam());
                        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                        if (!spawnEvent.isCancelled()) {
                            event.getPlayer().teleport(spawn.getLocation());
                        }
                    } else {
                        event.getPlayer().setHealth(0);
                    }
                } else if (event.getNewTeam().isObserver()) {
                    TeamModule teamModule = event.getNewTeam();
                    SpawnModule spawn = null;
                    for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                        if (spawnModule.getTeam().equals(teamModule)) spawn = spawnModule;
                    }
                    PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn, event.getNewTeam());
                    Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                    if (!spawnEvent.isCancelled()) {
                        event.getPlayer().setHealth(0);
                    }
                } else {
                    event.getPlayer().setHealth(0);
                }
            } catch (NullPointerException e) {

            }
        }

    }

}
