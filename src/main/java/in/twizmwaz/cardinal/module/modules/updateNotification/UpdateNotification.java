package in.twizmwaz.cardinal.module.modules.updateNotification;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.GitUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
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
                    BaseComponent[] chat = ComponentSerializer.parse(GitUtil.getUpdateMessage(notification));
                    event.getPlayer().sendMessage(chat);
                } catch (IOException ignored) {
                }
            }
        });
    }

}