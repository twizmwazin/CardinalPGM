package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class FlagEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public abstract Flag getFlag();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
