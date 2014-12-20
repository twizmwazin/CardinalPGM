package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class Kit implements Module {

    public static List<Kit> kits = new ArrayList<>();

    private String name;
    private List<KitItem> items;
    private List<KitArmor> armor;
    private List<PotionEffect> effects;
    private String parent;
    private boolean force;
    private boolean potionParticles;
    //unimplemented
    private boolean resetPearls;
    private boolean clear;
    private boolean clearItems;
    private int health;
    private float saturation;
    private int foodLevel;
    private float walkSpeed;
    private float knockback;
    //unimplemented
    private boolean jump;

    protected Kit(String name, List<KitItem> items, List<KitArmor> armor, List<PotionEffect> effects, String parent, boolean force, boolean potionParticles, boolean resetPearls, boolean clear, boolean clearItems, int health, float saturation, int foodLevel, float walkSpeed, float knockback ,boolean jump) {
        kits.add(this);
        this.name = name;
        this.items = items;
        this.armor = armor;
        this.effects = effects;
        this.parent = parent;
        this.force = force;
        this.potionParticles = potionParticles;
        this.resetPearls = resetPearls;
        this.clear = clear;
        this.clearItems = clearItems;
        this.health = health;
        this.saturation = saturation;
        this.foodLevel = foodLevel;
        this.walkSpeed = walkSpeed;
        this.knockback = knockback;
        this.jump = jump;

    }

    @Override
    public void unload() {
        kits.remove(this);
    }

    public void apply(Player player) {
        if (clear || clearItems) player.getInventory().clear();
        if (clear) {
            for (ItemStack armor : player.getInventory().getArmorContents()) {
                armor.setAmount(0);
            }
        }
        try {
            getKitByName(parent).apply(player);
        } catch (NullPointerException e) {

        }
        if (health > -1) {
            if (force) player.setHealth(health);
            else player.setHealth(player.getHealth() + health);
        }
        if (saturation > -1) {
            if (force) player.setSaturation(saturation);
            else player.setSaturation(player.getSaturation() + saturation);
        }
        if (foodLevel > -1) {
            if (force) player.setFoodLevel(foodLevel);
            else player.setFoodLevel(player.getFoodLevel() + foodLevel);
        }
        player.setWalkSpeed(walkSpeed);
        player.setKnockbackReduction(knockback);
        for (KitItem item : this.items) {
            if (player.getInventory().getItem(item.getSlot()) == null || force) {
                player.getInventory().setItem(item.getSlot(), item.getItem());
            } else {
                player.getInventory().addItem(item.getItem());
            }
        }
        for (KitArmor armor : this.armor) {
            switch (armor.getType()) {
                case HELMET: player.getInventory().setHelmet(armor.getItem());
                    break;
                case CHESTPLATE: player.getInventory().setChestplate(armor.getItem());
                    break;
                case LEGGINGS: player.getInventory().setLeggings(armor.getItem());
                    break;
                case BOOTS: player.getInventory().setBoots(armor.getItem());
                    break;
            }
        }
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
            player.addIgnorantEffect(effect.getType());
        }
        player.setPotionParticles(potionParticles);
    }

    public String getName() {
        return name;
    }

    public static Kit getKitByName(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(name)) return kit;
        }
        return null;
    }

}
