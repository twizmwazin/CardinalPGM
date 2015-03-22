package in.twizmwaz.cardinal.module.modules.chat;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatModule implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasMetadata("default-channel")) {
            event.setCancelled(true);
            Bukkit.dispatchCommand(event.getPlayer(), String.valueOf(event.getPlayer().getMetadata("default-channel").get(0).asString().charAt(0)) + " " + event.getMessage());
        } else {
            event.setCancelled(true);
            Bukkit.dispatchCommand(event.getPlayer(), "t " + event.getMessage());
        }
    }
}
