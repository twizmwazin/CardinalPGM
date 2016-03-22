package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class HealthKit implements Kit {

    private int health;
    private int hunger;
    private float saturation;

    public HealthKit(int health, int hunger, float saturation) {
        this.health = health;
        this.hunger = hunger;
        this.saturation = saturation;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        if(health != -1 && (force || health > player.getHealth())) player.setHealth(health);
        if(hunger != -1 && (force || hunger > player.getFoodLevel())) player.setFoodLevel(hunger);
        if(saturation != 0 && (force || saturation > player.getSaturation())) player.setSaturation(saturation);
    }

}
