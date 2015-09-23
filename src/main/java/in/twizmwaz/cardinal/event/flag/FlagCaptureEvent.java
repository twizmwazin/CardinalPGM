package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import org.bukkit.entity.Player;

public class FlagCaptureEvent extends FlagEvent {

    private Player player;
    private FlagObjective flagObjective;
    private Net net;

    public FlagCaptureEvent(Player player, FlagObjective flagObjective, Net net) {
        this.player = player;
        this.flagObjective = flagObjective;
        this.net = net;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public FlagObjective getFlagObjective() {
        return flagObjective;
    }

    public Net getNet() {
        return net;
    }

}
