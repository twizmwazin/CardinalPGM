package in.twizmwaz.cardinal.module.modules.updateNotification;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.UpdateHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.GitUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class UpdateNotification implements Module {

    private static UpdateHandler updateHandler;

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp()) {
            try {
                updateHandler = new UpdateHandler();
            } catch (IOException e) {
                Bukkit.getLogger().warning("Could not retrieve updates");
            }
            if (updateHandler != null) {
                if (updateHandler.checkUpdates())
                    updateHandler.sendUpdateMessage(event.getPlayer());
            }
        }
    }

}