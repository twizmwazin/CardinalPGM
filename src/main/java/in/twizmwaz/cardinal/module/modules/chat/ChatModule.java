package in.twizmwaz.cardinal.module.modules.chat;

import in.twizmwaz.cardinal.chat.TeamChat;
import in.twizmwaz.cardinal.module.Module;
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
        TeamChat.sendTeamMessage(event.getMessage(), event.getPlayer());
        event.setCancelled(true);
    }

}
