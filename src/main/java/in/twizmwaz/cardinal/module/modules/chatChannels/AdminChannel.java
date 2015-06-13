package in.twizmwaz.cardinal.module.modules.chatChannels;

import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class AdminChannel extends PrivateChannel {

    public AdminChannel(PermissionModule permissionModule) {
        super(new Permission("cardinal.mod"), permissionModule);
    }

    @Override
    public void sendMessage(String string) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) player.sendMessage(string);
        }
    }

    @Override
    public void sendLocalizedMessage(ChatMessage message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) player.sendMessage(message.getMessage(player.getLocale()));
        }
    }

    @Override
    public void unload() {
        resetMembers();
    }
}
