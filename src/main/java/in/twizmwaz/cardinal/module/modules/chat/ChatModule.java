package in.twizmwaz.cardinal.module.modules.chat;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.settings.Settings;
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
        switch (Settings.getSettingByName("ChatChannel").getValueByPlayer(event.getPlayer()).getValue()) {
            case "global":
                Bukkit.dispatchCommand(event.getPlayer(), "g " + event.getMessage());
                break;
            case "admin":
                Bukkit.dispatchCommand(event.getPlayer(), "a " + event.getMessage());
                break;
            case "team":
            default:
                Bukkit.dispatchCommand(event.getPlayer(), "t " + event.getMessage());
                break;
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        PermissionModule module = GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class);
        if (!GameHandler.getGameHandler().getGlobalMute()) {
            if (module.isMuted(event.getPlayer()) && (event.getMessage().toLowerCase().startsWith("/me ") || event.getMessage().toLowerCase().startsWith("/minecraft:me "))) {
                event.getPlayer().sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NO_PERMISSION).getMessage(event.getPlayer().getLocale()));
                event.setCancelled(true);
            }
        } else {
            if (event.getMessage().toLowerCase().startsWith("/me ") || event.getMessage().toLowerCase().startsWith("/minecraft:me ")) {
                event.getPlayer().sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NO_PERMISSION).getMessage(event.getPlayer().getLocale()));
                event.setCancelled(true);
            }
        }
    }
}
