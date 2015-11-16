package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlagEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private FlagObjective flag;

    public FlagEvent(FlagObjective flag) {
        this.flag = flag;
    }

    public FlagObjective getFlag() {
        return flag;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
