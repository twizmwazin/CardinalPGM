package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NetLeaveEvent extends FlagEvent {

    private Player player;
    private Net net;
    private Flag flag;

    public NetLeaveEvent(Player player, Net net, Flag flag) {
        this.player = player;
        this.net = net;
        this.flag = flag;
    }

    public Player getPlayer() {
        return player;
    }

    public Net getNet() {
        return net;
    }

    @Override
    public Flag getFlag() {
        return flag;
    }
}
