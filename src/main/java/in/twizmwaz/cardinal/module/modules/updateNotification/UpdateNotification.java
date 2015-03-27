package in.twizmwaz.cardinal.module.modules.updateNotification;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.UpdateHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.io.IOException;

public class UpdateNotification implements Module {

    private static UpdateHandler updateHandler;

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        try {
            updateHandler = new UpdateHandler();
        } catch (IOException e) {
            updateHandler.setUpdate(false);
            updateHandler.setMessage("Could not get update information");
        }
    }

}
