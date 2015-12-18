package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitApplyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Kit kit;
    private Player player;

    public KitApplyEvent(Kit kit, Player player) {
        this.kit = kit;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Kit getKit() {
        return kit;
    }

    public Player getPlayer() {
        return player;
    }


}
