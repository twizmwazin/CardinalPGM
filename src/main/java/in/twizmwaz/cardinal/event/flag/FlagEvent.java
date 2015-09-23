package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class FlagEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public abstract FlagObjective getFlagObjective();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
