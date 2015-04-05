package in.twizmwaz.cardinal.module.modules.updateNotification;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.GitUtils;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class UpdateNotification implements Module {

    private final String notification = "https://raw.githubusercontent.com/twizmwazin/CardinalNotifications/master/update.json";

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    IChatBaseComponent chat = ChatSerializer.a(GitUtils.getUpdateMessage(notification));
                    PacketPlayOutChat packet = new PacketPlayOutChat(chat);
                    ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                } catch (IOException ignored) {}
            }
        });
    }

}