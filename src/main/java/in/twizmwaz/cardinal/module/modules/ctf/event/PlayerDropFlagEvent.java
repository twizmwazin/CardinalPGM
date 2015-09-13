package in.twizmwaz.cardinal.module.modules.ctf.event;

import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDropFlagEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Flag flag;

    public PlayerDropFlagEvent(Player player, Flag flag) {
        this.player = player;
        this.flag = flag;
    }

    public Player getPlayer() {
        return player;
    }

    public Flag getFlag() {
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
