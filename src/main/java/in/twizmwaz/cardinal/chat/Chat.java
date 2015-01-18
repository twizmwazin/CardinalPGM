package in.twizmwaz.cardinal.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Chat implements Listener {

    public Chat(JavaPlugin plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        TeamChat.sendTeamMessage(event.getMessage(), event.getPlayer());
        event.setCancelled(true);
    }
}
