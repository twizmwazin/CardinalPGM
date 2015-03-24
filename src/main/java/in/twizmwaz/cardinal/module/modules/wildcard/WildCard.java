package in.twizmwaz.cardinal.module.modules.wildcard;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class WildCard implements Module {

    protected WildCard() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("cardinal.wildcard")) {
            String command = event.getMessage() + " ";
            if (command.contains(" * ")) {
                event.setCancelled(true);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Bukkit.dispatchCommand(event.getPlayer(), command.substring(1).replaceAll(" \\* ", " " + player.getName() + " ").trim());
                }
            }
        }
    }

}
