package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import org.bukkit.entity.Player;

public class NetLeaveEvent extends FlagEvent {

    private Player player;
    private Net net;
    private FlagObjective flagObjective;

    public NetLeaveEvent(Player player, Net net, FlagObjective flagObjective) {
        this.player = player;
        this.net = net;
        this.flagObjective = flagObjective;
    }

    public Player getPlayer() {
        return player;
    }

    public Net getNet() {
        return net;
    }

    @Override
    public FlagObjective getFlagObjective() {
        return flagObjective;
    }
}
