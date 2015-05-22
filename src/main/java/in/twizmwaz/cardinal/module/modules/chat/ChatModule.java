package in.twizmwaz.cardinal.module.modules.chat;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatModule implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        if (event.getPlayer().hasMetadata("default-channel")) {
            Bukkit.dispatchCommand(event.getPlayer(), String.valueOf(event.getPlayer().getMetadata("default-channel").get(0).asString().charAt(0)) + " " + event.getMessage());
        } else {
            Bukkit.dispatchCommand(event.getPlayer(), "t " + event.getMessage());
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        PermissionModule module = GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class);
        if (module.isMuted(event.getPlayer()) && event.getMessage().startsWith("/me ")) {
            event.getPlayer().sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NO_PERMISSION).getMessage(event.getPlayer().getLocale()));
            event.setCancelled(true);
        }
    }
}
