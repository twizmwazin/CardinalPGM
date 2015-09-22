package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlagCaptureEvent extends FlagEvent {

    private Player player;
    private Flag flag;
    private Net net;

    public FlagCaptureEvent(Player player, Flag flag, Net net) {
        this.player = player;
        this.flag = flag;
        this.net = net;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Flag getFlag() {
        return flag;
    }

    public Net getNet() {
        return net;
    }

}
