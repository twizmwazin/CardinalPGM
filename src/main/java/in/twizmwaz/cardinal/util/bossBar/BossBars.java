package in.twizmwaz.cardinal.util.bossBar;

import in.twizmwaz.cardinal.chat.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBars implements Listener {

    public static Map<UUID, LocalizedBossBar> broadcastedBossBars = new HashMap<>();

    public static UUID addBroadcastedBossBar(ChatMessage bossBarTitle, BarColor color, BarStyle style, Boolean shown, BarFlag... flags) {
        UUID id = UUID.randomUUID();
        LocalizedBossBar bossBar = new LocalizedBossBar(bossBarTitle, color, style, flags);
        bossBar.setVisible(shown);
        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }
        broadcastedBossBars.put(id, bossBar);
        return id;
    }

    public static void removeBroadcastedBossBar(UUID id) {
        if (broadcastedBossBars.containsKey(id)) {
            broadcastedBossBars.get(id).setVisible(false);
            broadcastedBossBars.get(id).removeAll();
            broadcastedBossBars.remove(id);
        }
    }

    public static void setTitle(UUID id, ChatMessage chat) {
        if (broadcastedBossBars.containsKey(id)) broadcastedBossBars.get(id).setTitle(chat);
    }

    public static void setProgress(UUID id, Double progress) {
        if (broadcastedBossBars.containsKey(id)) broadcastedBossBars.get(id).setProgress(progress);
    }

    public static void setVisible(UUID id, Boolean visible) {
        if (broadcastedBossBars.containsKey(id)) broadcastedBossBars.get(id).setVisible(visible);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        for (LocalizedBossBar bossBar : broadcastedBossBars.values()) {
            bossBar.addPlayer(player);
        }
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        for (LocalizedBossBar bossBar : broadcastedBossBars.values()) {
            bossBar.removePlayer(event.getPlayer());
        }
    }


}
