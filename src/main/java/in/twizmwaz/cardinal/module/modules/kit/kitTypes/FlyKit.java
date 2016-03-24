package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.KitRemovable;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;


public class FlyKit implements KitRemovable {

    private boolean canFly;
    private Boolean flying;
    private float flySpeed;

    public FlyKit(boolean canFly, Boolean flying, float flySpeed) {
        this.canFly = canFly;
        this.flying = flying;
        this.flySpeed = flySpeed;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        player.setAllowFlight(canFly);
        if (flying != null) player.setFlying(flying);
        player.setFlySpeed(flySpeed);
    }

    @Override
    public void remove(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setFlySpeed(0.1f);
    }

}
