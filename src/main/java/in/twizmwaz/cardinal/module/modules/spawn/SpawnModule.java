package in.twizmwaz.cardinal.module.modules.spawn;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.teams.spawns.Spawn;
import in.twizmwaz.cardinal.util.PlayerUtil;
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
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnModule implements Module {

    private final JavaPlugin plugin;
    private final Match match;

    protected SpawnModule(Match match) {
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
            if (!match.getTeam(event.getPlayer()).isObserver()) {
                event.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
            event.getPlayer().updateInventory();
        } catch (NullPointerException e) {
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInitLogin(PlayerInitialSpawnEvent event) {
        Spawn spawn = match.getTeamById("observers").getSpawn();
        PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn, match.getTeamById("observers"));
        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
        if (!spawnEvent.isCancelled()) {
            event.setSpawnLocation(spawn.getPoint().toLocation());
            PlayerUtil.resetPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onMinecraftRespawn(PlayerRespawnEvent event) {
        Spawn spawn = match.getTeam(event.getPlayer()).getSpawn();
        PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn, match.getTeam(event.getPlayer()));
        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
        if (!spawnEvent.isCancelled()) {
            event.setRespawnLocation(spawn.getPoint().toLocation());
        }
    }


    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!match.getTeam(player).isObserver()) {
                PlayerUtil.resetPlayer(player);
                Spawn spawn = match.getTeam(player).getSpawn();
                PgmSpawnEvent spawnEvent = new PgmSpawnEvent(player, spawn, match.getTeam(player));
                Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                if (!spawnEvent.isCancelled()) {
                    player.teleport(spawn.getPoint().toLocation());
                }
            }
        }
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Spawn spawn = match.getTeamById("observers").getSpawn();
            PgmSpawnEvent spawnEvent = new PgmSpawnEvent(player, spawn, match.getTeamById("observers"));
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                PlayerUtil.resetPlayer(player);
                player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
                player.teleport(spawn.getPoint().toLocation());
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
                    PlayerUtil.resetPlayer(event.getPlayer());
                    Spawn spawn = event.getNewTeam().getSpawn();
                    PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn, event.getNewTeam());
                    Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                    if (!spawnEvent.isCancelled()) {
                        event.getPlayer().teleport(spawn.getPoint().toLocation());
                    }
                } else if (event.getNewTeam().isObserver()) {
                    Spawn spawn = event.getNewTeam().getSpawn();
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
