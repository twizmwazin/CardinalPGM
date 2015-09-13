package in.twizmwaz.cardinal.module.modules.ctf.event;

import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveNetEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Net net;

    public PlayerLeaveNetEvent(Player player, Net net) {
        this.player = player;
        this.net = net;
    }

    public Player getPlayer() {
        return player;
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
