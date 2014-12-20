package in.twizmwaz.cardinal.match.listeners;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.JoinType;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInitialSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

public class ConnectionListener implements Listener {

    private final JavaPlugin plugin;
    private final Match match;

    public ConnectionListener(JavaPlugin plugin, Match match) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.match = match;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setScoreboard(match.getScoreboard());
        GameHandler.getGameHandler().getMatch().getTeamById("observers").add(event.getPlayer(), JoinType.JOIN);
        event.getPlayer().getInventory().clear();
        for (ItemStack armor : event.getPlayer().getInventory().getArmorContents()) {
            armor.setAmount(0);
        }
        for (PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
            event.getPlayer().removePotionEffect(effect.getType());
        }
        event.getPlayer().clearIgnorantEffects();
        event.getPlayer().getInventory().setItem(0, new ItemStack(Material.COMPASS));

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInitLogin(PlayerInitialSpawnEvent event) {
        event.setSpawnLocation(match.getTeamById("observers").getSpawnPoint());
        event.getPlayer().setGameMode(GameMode.CREATIVE);
        //event.getPlayer().setFlying(false);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }

    public void onPlayerLeave(PlayerKickEvent event) {
        removePlayer(event.getPlayer());
    }

    private void removePlayer(Player player) {
        for (PgmTeam team : match.getTeams()) {
            if (team.hasPlayer(player)) {
                team.remove(player);
            }
        }
    }

}
