package in.twizmwaz.cardinal.event.flag;

import in.twizmwaz.cardinal.module.modules.ctf.Flag;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlagPickupEvent extends FlagEvent {

    private Player player;
    private Flag flag;

    public FlagPickupEvent(Player player, Flag flag) {
        this.player = player;
        this.flag = flag;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Flag getFlag() {
        return flag;
    }

}
