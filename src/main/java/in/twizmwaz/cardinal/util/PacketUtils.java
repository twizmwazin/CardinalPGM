package in.twizmwaz.cardinal.util;

import net.minecraft.server.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class PacketUtils {

    public static void sendPacket(Player player, Packet packet) {
        if (player == null) return;
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendPacket(UUID player, Packet packet) {
        sendPacket(Bukkit.getPlayer(player), packet);
    }

    public static void broadcastPacket(Packet packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPacket(player, packet);
        }
    }

    public static void broadcastPacket(Packet packet, List<Player> players) {
        for (Player player : players) {
            sendPacket(player, packet);
        }
    }

    public static void broadcastPacketByUUID(Packet packet, List<UUID> players) {
        for (UUID player : players) {
            sendPacket(player, packet);
        }
    }

    public static void setField(String field, Object object, Object value) {
        try {
            Field entryField = object.getClass().getDeclaredField(field);
            entryField.setAccessible(true);
            entryField.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
