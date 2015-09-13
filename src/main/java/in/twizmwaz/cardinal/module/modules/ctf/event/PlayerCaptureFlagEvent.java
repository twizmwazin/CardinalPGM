package in.twizmwaz.cardinal.module.modules.ctf.event;

import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCaptureFlagEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Flag flag;
    private Net net;

    public PlayerCaptureFlagEvent(Player player, Flag flag, Net net) {
        this.player = player;
        this.flag = flag;
        this.net = net;
    }

    public Player getPlayer() {
        return player;
    }

    public Flag getFlag() {
        return flag;
    }

    public Net getNet() {
        return net;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
