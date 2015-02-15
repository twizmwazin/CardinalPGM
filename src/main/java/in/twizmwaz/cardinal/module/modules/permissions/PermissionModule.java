package in.twizmwaz.cardinal.module.modules.permissions;

import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class PermissionModule implements Module {
    
    private final Plugin plugin;
    private final Map<Player, PermissionAttachment> attachmentMap;
    
    public PermissionModule(Plugin plugin) {
        this.plugin = plugin;
        this.attachmentMap = new HashMap<Player, PermissionAttachment>();
    }
    
    @Override
    public void unload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.removeAttachment(attachmentMap.get(player));
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        attachmentMap.put(event.getPlayer(), event.getPlayer().addAttachment(plugin));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers())
        attachmentMap.put(player, player.addAttachment(plugin));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.getPlayer().removeAttachment(attachmentMap.get(event.getPlayer()));
        attachmentMap.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        event.getPlayer().removeAttachment(attachmentMap.get(event.getPlayer()));
        attachmentMap.remove(event.getPlayer());
    }
    
    public PermissionAttachment getPlayerAttachment(Player player) {
        return attachmentMap.get(player);
    }
    
}
