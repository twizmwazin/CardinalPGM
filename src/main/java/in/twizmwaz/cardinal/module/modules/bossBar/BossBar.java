package in.twizmwaz.cardinal.module.modules.bossBar;

import com.google.common.collect.Maps;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;

public class BossBar implements Module {

    private final Map<Player, FakeWither> players = Maps.newHashMap();

    protected static int ENTITY_DISTANCE = 32;

    @Override
    public void unload() {
        for (Player player : players.keySet()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(getWither(player, "").getDestroyPacket());
            players.remove(player);
        }
        HandlerList.unregisterAll(this);
    }

    public void sendMessage(Player player, ChatMessage message, float percent) {
        Validate.isTrue(0F <= percent && percent <= 100F, "Percent must be between 0F and 100F, but was: ", percent);
        FakeWither wither = players.get(player);
        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.WAITING || GameHandler.getGameHandler().getMatch().getState() == MatchState.ENDED) {
            BossBar.delete();
        }
        if (wither == null) {
            addWither(player, message.getMessage(player.getLocale()), true);
            handleTeleport(player, player.getLocation(), true);
            wither = players.get(player);
        }
        if (wither != null) {
            wither.name = checkMessageLength(message.getMessage(player.getLocale()));
            wither.health = percent * 100F / wither.getMaxHealth();
        }
        updateWither(wither, player);
    }

    private String checkMessageLength(String message) {
        if (message.length() > 64) {
            message = message.substring(0, 63);
        }
        return message;
    }

    public void handleTeleport(final Player player, final Location location, final boolean visible) {
        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.CYCLING || GameHandler.getGameHandler().getMatch().getState() == MatchState.STARTING || GameHandler.getGameHandler().getMatch().getState() == MatchState.PLAYING) {
            if (players.containsKey(player)) {
                Bukkit.getScheduler().runTaskLater(Cardinal.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        FakeWither oldWither = getWither(player, "");
                        float health = oldWither.health;
                        String message = oldWither.name;
                        if (oldWither.isVisible()) {
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(getWither(player, "").getDestroyPacket());
                        }
                        players.remove(player);
                        FakeWither wither = addWither(player, message, visible);
                        wither.health = health;
                        updateWither(wither, player);
                    }
                }, 1L);
            }
        }
    }

    public void updateWither(FakeWither wither, Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(wither.getMetaPacket(wither.updateDataWatcher()));
        connection.sendPacket(wither.getTeleportPacket(getWitherLocation(player)));
    }

    public FakeWither getWither(Player player, String message) {
        if (players.containsKey(player)) {
            return players.get(player);
        } else {
            return addWither(player, checkMessageLength(message), false);
        }
    }

    public FakeWither addWither(Player player, String message, boolean visible) {
        FakeWither wither = new FakeWither(player, message, getWitherLocation(player));
        wither.setVisible(visible);
        if (visible) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(wither.getSpawnPacket());
        }
        players.put(player, wither);
        return wither;
    }

    private Location getWitherLocation(Player player) {
        Location loc = player.getEyeLocation();
        loc.setPitch(loc.getPitch() - 20);
        return player.getEyeLocation().add(loc.getDirection().multiply(ENTITY_DISTANCE));
    }

    private void destroy(Player player) {
        FakeWither wither = players.get(player);
        if (wither == null) return;
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(wither.getDestroyPacket());
        players.remove(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        players.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLogout(PlayerQuitEvent event) {
        players.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMatchEnd(MatchEndEvent event) {
        BossBar.delete();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCycleComplete(CycleCompleteEvent event) {
        BossBar.delete();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (players.get(event.getPlayer()) != null) {
            handleTeleport(event.getPlayer(), event.getTo(), true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        if (players.get(event.getPlayer()) != null) {
            handleTeleport(event.getPlayer(), event.getPlayer().getLocation(), true);
        }
    }

    public static void sendGlobalBossBar(ChatMessage message, float percent) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendBossBar(player, message, percent);
        }
    }

    public static void sendBossBar(Player player, ChatMessage message, float percent) {
        GameHandler.getGameHandler().getMatch().getModules().getModule(BossBar.class).sendMessage(player, message, percent);
    }

    public static void delete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            GameHandler.getGameHandler().getMatch().getModules().getModule(BossBar.class).destroy(player);
        }
    }

}
