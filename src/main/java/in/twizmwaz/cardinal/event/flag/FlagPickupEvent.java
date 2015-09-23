package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import org.bukkit.entity.Player;

public class FlagPickupEvent extends FlagEvent {

    private Player player;
    private FlagObjective flagObjective;

    public FlagPickupEvent(Player player, FlagObjective flagObjective) {
        this.player = player;
        this.flagObjective = flagObjective;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public FlagObjective getFlagObjective() {
        return flagObjective;
    }

}
