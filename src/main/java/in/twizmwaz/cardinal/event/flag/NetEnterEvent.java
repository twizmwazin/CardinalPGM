package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import org.bukkit.entity.Player;

public class NetEnterEvent extends FlagEvent {

    private Player player;
    private Net net;

    public NetEnterEvent(Player player, Net net, FlagObjective flagObjective) {
        super(flagObjective);
        this.player = player;
        this.net = net;
    }

    public Player getPlayer() {
        return player;
    }

    public Net getNet() {
        return net;
    }
}
