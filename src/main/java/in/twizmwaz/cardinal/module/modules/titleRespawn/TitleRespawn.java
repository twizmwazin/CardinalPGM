package in.twizmwaz.cardinal.module.modules.titleRespawn;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.util.PlayerUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * This module adds a new death and respawn system from the Overcast Network.
 */
public class TitleRespawn implements TaskedModule {
    // Module's global settings
    public static final PotionEffect[] HORSE_POTIONS = {
        // Make the horse invisible for players, so they can't see them
        new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1),
        // Prevert the horse from moving
        new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0)
    };

    // Module's settings
    private final int delay;
    private final boolean auto;
    private final boolean blackout;
    // ^ not really sure what is this - is it
    // https://github.com/twizmwazin/CardinalPGM/issues/699#issuecomment-111310151 ?
    private final boolean spectate;
    private final boolean bed;

    // Local variables
    private final Map<UUID, Long> deadPlayers = new HashMap<>();

    public TitleRespawn(int delay, boolean auto, boolean blackout, boolean spectate, boolean bed) {
        this.delay = delay;
        this.auto = auto;
        this.blackout = blackout;
        this.spectate = spectate;
        this.bed = bed;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    /**
     * This method is invoked 10 times in one second to update players titles
     * and respawn them too. The BukkitScheduler is running with the 2 ticks
     * delay because 20 (ticks) / 10 (times) = 2
     */
    @Override
    public void run() {
        // Loop all dead players and check them
        for (UUID id : this.deadPlayers.keySet()) {
            Player player = GameHandler.getGameHandler().getPlugin().getServer().getPlayer(id);
            if (this.canRespawn(id)) {
                if (this.auto) {
                    this.respawnPlayer(player);
                } else {
                    // TODO send a sub-title "Left click to respawn"
                }
            } else {
                // TODO send a sub-title "Left click to respawn in Xs"
            }
        }
    }

    /**
     * Checks if the specifited player can respawn by their UUID
     * @param id UUID of player to check
     * @return <code>true</code> if the player can respawn, otherwise
     * <code>false</code>.
     */
    public boolean canRespawn(UUID id) {
        return this.deadPlayers.getOrDefault(id, Long.MIN_VALUE) <= System.currentTimeMillis();
    }

    /**
     * Respawn a player, clear the title and remove him from the death players.
     * @param player to be respawned
     */
    public void respawnPlayer(Player player) {
        // TODO kill the horse

        boolean bedLocation = false;
        Location location = player.getWorld().getSpawnLocation();

        // Set the location to the bed location if it's enabled and player has a
        // bed spawn location
        if (this.bed && player.getBedSpawnLocation() != null) {
            bedLocation = true;
            location = player.getBedSpawnLocation();
        }

        // Call PlayerRespawnEvent to invoke all listener, and get the new spawn
        // location from other Cardinal's modules.
        PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(player, location, bedLocation);
        GameHandler.getGameHandler().getPlugin().getServer().getPluginManager().callEvent(respawnEvent);

        // Player is already reseted to the default clear state in the death
        // event. We need only to set his gamemode to the survival mode.
        player.setGameMode(GameMode.SURVIVAL);
        // There is no respawn cause, so we use UNKNOWN. PLUGIN is a plugin, not
        // this fake respawn!
        player.teleport(respawnEvent.getRespawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        this.deadPlayers.remove(player.getUniqueId());

        // Show this player to other online players
        for (Player online : GameHandler.getGameHandler().getPlugin().getServer().getOnlinePlayers()) {
            online.showPlayer(player);
        }
    }

    /**
     * The main event listener of this module that listen the deaths
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.getHealth() != 0) {
                return;
            }

            this.deadPlayers.put(player.getUniqueId(), System.currentTimeMillis() + this.delay * 1000);

            // We need to handle PlayerDeathEvent because this
            // will broke many of Bukkit plugins.
            PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, Arrays.asList(player.getInventory().getContents()), player.getExpToLevel(), 0, 0, 0, "%s died because of respawn");
            GameHandler.getGameHandler().getPlugin().getServer().getPluginManager().callEvent(deathEvent);

            // We need to broadcast this death-message too, because NMS server
            // is not invoked by the event.
            if (deathEvent.getDeathMessage() != null) {
                GameHandler.getGameHandler().getPlugin().getServer().broadcastMessage(String.format(deathEvent.getDeathMessage(), player.getDisplayName() + ChatColor.RESET));
            }

            // Hide this player from other online players
            for (Player online : GameHandler.getGameHandler().getPlugin().getServer().getOnlinePlayers()) {
                online.hidePlayer(player);
            }

            player.setGameMode(GameMode.CREATIVE);
            PlayerUtils.resetPlayer(player);

            if (!this.spectate) {
                // We can do setWalkingSpeed(0.0), and give him potion speed 0, but
                // he can jump. Overcast Network spawns a horse (?) and sets player
                // as the passanger. We can do it by spawning a horse, giving
                // invincible potion and sitting the player on the horse.

                Horse fakeHorse = GameHandler.getGameHandler().getMatchWorld().spawn(player.getLocation(), Horse.class);
                fakeHorse.setVariant(Horse.Variant.HORSE);
                for (PotionEffect potion : HORSE_POTIONS) fakeHorse.addPotionEffect(potion);
                fakeHorse.setPassenger(player);
            }

            // TODO send only a title (not sub-title) "You died!" to the player
            // subtitle should be sent by the run() method
        }
    }

    /**
     * Event listener that listen to left mouse click to handle the respawn
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            // Handle only if the auto mode is not enabled. Otherwise they should be
            // auto respawned
            if (!this.auto && this.deadPlayers.containsKey(e.getPlayer().getUniqueId())) {
                long millis = this.deadPlayers.get(e.getPlayer().getUniqueId());

                if (this.canRespawn(e.getPlayer().getUniqueId())) {
                    this.respawnPlayer(e.getPlayer());
                }
            }
        }
    }

    /**
     * Listen to match start to begin allow respawning players
     */
    @EventHandler
    public void onMatchStart(MatchStartEvent e) {
        GameHandler.getGameHandler().getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(GameHandler.getGameHandler().getPlugin(), this, 0L, 2L);
    }

    // TODO handle MANY, MANY events to prevert dead people to interact with the
    // gameplay. I think we should do it in the ObserversModule, because
    // listeners there are finished.

}
