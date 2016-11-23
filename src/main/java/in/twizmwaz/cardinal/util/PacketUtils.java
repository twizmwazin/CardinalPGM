package in.twizmwaz.cardinal.util;

import com.google.common.collect.Lists;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutEntityMetadata;
import net.minecraft.server.PacketPlayOutUpdateAttributes;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collections;
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

    @SuppressWarnings("unchecked")
    public static <T> T getField(String field, Object object, Class<T> outputType) {
        try {
            Field entryField = object.getClass().getDeclaredField(field);
            entryField.setAccessible(true);
            return (T) entryField.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PacketPlayOutEntityMetadata createMetadataPacket(int id, List<DataWatcher.Item<?>> metadata) {
        PacketPlayOutEntityMetadata result = new PacketPlayOutEntityMetadata();
        result.a = id;
        result.b = metadata;
        return result;
    }

    @SuppressWarnings("unchecked")
    public static PacketPlayOutUpdateAttributes createHealthAttribute(int id) {
        PacketPlayOutUpdateAttributes result = new PacketPlayOutUpdateAttributes(id, Lists.newArrayList());
        getField("b", result, List.class).add(result.new AttributeSnapshot("generic.maxHealth", 0D, Collections.EMPTY_LIST));
        return result;
    }

}
