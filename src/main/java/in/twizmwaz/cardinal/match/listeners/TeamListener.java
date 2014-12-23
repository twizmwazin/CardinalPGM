package in.twizmwaz.cardinal.match.listeners;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.teams.spawns.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

public class TeamListener implements Listener {

    private final JavaPlugin plugin;
    private final Match match;

    public TeamListener(JavaPlugin plugin, Match match) {
        this.plugin = plugin;
        this.match = match;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (match.getState().equals(MatchState.PLAYING)) {
            try {
                if (event.getOldTeam().isObserver()) {
                    event.getPlayer().getInventory().clear();
                    for (ItemStack armor : event.getPlayer().getInventory().getArmorContents()) {
                        armor.setType(Material.AIR);
                    }
                    for (PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
                        event.getPlayer().removePotionEffect(effect.getType());
                    }
                    event.getPlayer().clearIgnorantEffects();
                    Spawn spawn = event.getNewTeam().getSpawn();
                    PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn);
                    Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                    if (!spawnEvent.isCancelled()) {
                        event.getPlayer().teleport(spawn.getPoint().toLocation(GameHandler.getGameHandler().getMatchWorld()));
                    }
                } else if (event.getNewTeam().isObserver()) {
                    Spawn spawn = event.getNewTeam().getSpawn();
                    PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn);
                    Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                    if (!spawnEvent.isCancelled()) {
                        event.getPlayer().setHealth(0);
                        event.getPlayer().setGameMode(GameMode.CREATIVE);
                        event.getPlayer().setAffectsSpawning(false);
                    }
                } else {
                    event.getPlayer().setHealth(0);
                }
            } catch (NullPointerException e) {

            }
        }
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMinecraftRespawn(PlayerRespawnEvent event) {
        if (match.getState().equals(MatchState.PLAYING) && !match.getTeam(event.getPlayer()).isObserver()) {
            Spawn spawn = match.getTeam(event.getPlayer()).getSpawn();
            PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer(), spawn);
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                event.getPlayer().teleport(spawn.getPoint().toLocation(GameHandler.getGameHandler().getMatchWorld()));
            }
        }
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!match.getTeam(player).isObserver()) {
                player.getInventory().clear();
                for (ItemStack armor : player.getInventory().getArmorContents()) {
                    armor.setType(Material.AIR);
                }
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                player.clearIgnorantEffects();
                Spawn spawn = match.getTeam(player).getSpawn();
                PgmSpawnEvent spawnEvent = new PgmSpawnEvent(player, spawn);
                Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                if (!spawnEvent.isCancelled()) {
                    player.teleport(spawn.getPoint().toLocation(GameHandler.getGameHandler().getMatchWorld()));
                }
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.CREATIVE);
        }
    }

    @EventHandler
    public void onPlayerSpawn(PgmSpawnEvent event) {
        event.getPlayer().setGameMode(GameMode.SURVIVAL);
        event.getPlayer().setAffectsSpawning(true);
    }


}
