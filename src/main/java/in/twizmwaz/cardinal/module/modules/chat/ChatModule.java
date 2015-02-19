package in.twizmwaz.cardinal.module.modules.chat;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.chatChannels.TeamChannel;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
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
        event.setCancelled(true);
        Bukkit.dispatchCommand(event.getPlayer(), "t " + event.getMessage());
    }
}
