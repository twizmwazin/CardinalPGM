package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import org.bukkit.entity.Player;

public class FlagPickupEvent extends FlagEvent {

    private Player player;

    public FlagPickupEvent(Player player, FlagObjective flagObjective) {
        super(flagObjective);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
