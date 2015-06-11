package in.twizmwaz.cardinal.module.modules.bossBar;

import com.google.common.collect.Maps;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.module.Module;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;

public class BossBar implements Module {

    private final Map<Player, FakeWither> players = Maps.newHashMap();

    @Override
    public void unload() {
        for (Player player : players.keySet()) {
            players.get(player);
        }
    }

    public void sendMessage(Player player, ChatMessage message, float percent) {
        Validate.isTrue(0F <= percent && percent <= 100F, "Percent must be between 0F and 100F, but was: ", percent);
        FakeWither wither = players.get(player);
        wither.name = checkMessageLength(message.getMessage(player.getLocale()));
        wither.health = (percent / 100f) * wither.getMaxHealth();
        sendWither(wither, player);
    }

    private String checkMessageLength(String message) {
        if (message.length() > 64) {
            message = message.substring(0, 63);
        }
        return message;
    }

    public void handleTeleport(final Player player, final Location location) {
        if (players.containsKey(player)) {
            Bukkit.getScheduler().runTaskLater(Cardinal.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (players.containsKey(player)) {
                        FakeWither oldwither = getWither(player, "");
                        float health = oldwither.health;
                        String message = oldwither.name;
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(getWither(player, "").getDestroyPacket());
                        players.remove(player);
                        FakeWither wither = addWither(player, location, message);
                        wither.health = health;
                        sendWither(wither, player);
                    }
                }
            }, 1L);
        }
    }

    void sendWither(FakeWither wither, Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(wither.getMetaPacket(wither.getWatcher()));
        connection.sendPacket(wither.getTeleportPacket(getWitherLocation(player)));
    }

    FakeWither getWither(Player player, String message) {
        if (players.containsKey(player)) {
            return players.get(player);
        } else {
            return addWither(player, checkMessageLength(message));
        }
    }

    FakeWither addWither(Player player, String message) {
        FakeWither wither = new FakeWither(message, getWitherLocation(player));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(wither.getSpawnPacket());
        players.put(player, wither);
        return wither;
    }

    private FakeWither addWither(Player player, Location loc, String message) {
        FakeWither wither = new FakeWither(message, getWitherLocation(player));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(wither.getSpawnPacket());
        players.put(player, wither);
        return wither;
    }

    private Location getWitherLocation(Player player) {
        return player.getLocation().add(player.getEyeLocation().getDirection().multiply(100));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PlayerLogout(PlayerQuitEvent event) {
        players.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(final PlayerMoveEvent event) {
        handleTeleport(event.getPlayer(), event.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        handleTeleport(event.getPlayer(), event.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
    }
}
