package in.twizmwaz.cardinal.module.modules.kit.kitTypes;


import in.twizmwaz.cardinal.module.modules.kit.KitRemovable;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class WalkSpeedKit implements KitRemovable {

    float walkSpeed;

    public WalkSpeedKit(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        player.setWalkSpeed(walkSpeed);
    }

    @Override
    public void remove(Player player) {
        player.setWalkSpeed(0.2f);
    }

}
