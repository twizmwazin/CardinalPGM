package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import org.bukkit.entity.Player;

public class FlagDropEvent extends FlagEvent {

    private Player player;

    public FlagDropEvent(Player player, FlagObjective flagObjective) {
        super(flagObjective);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
