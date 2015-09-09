package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.KitApplyEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Kit implements Module {

    private String name;
    private List<KitItem> items;
    private List<KitArmor> armor;
    private List<PotionEffect> effects;
    private List<KitBook> books;
    private String parent;
    private boolean force;
    private boolean potionParticles;
    private boolean resetPearls;
    private boolean clear;
    private boolean clearItems;
    private int health;
    private float saturation;
    private int foodLevel;
    private float walkSpeed;
    private float knockback;
    private boolean jump; //unimplemented
    private KitFly fly;

    protected Kit(String name, List<KitItem> items, List<KitArmor> armor, List<PotionEffect> effects, List<KitBook> books, String parent, boolean force, boolean potionParticles, boolean resetPearls, boolean clear, boolean clearItems, int health, float saturation, int foodLevel, float walkSpeed, float knockback, boolean jump, KitFly fly) {
        this.name = name;
        this.items = items;
        this.armor = armor;
        this.effects = effects;
        this.books = books;
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
        this.fly = fly;
    }

    public static Kit getKitByName(String name) {
        for (Kit kit : GameHandler.getGameHandler().getMatch().getModules().getModules(Kit.class)) {
            if (kit.getName().equalsIgnoreCase(name)) return kit;
        }
        return null;
    }

    @Override
    public void unload() {
    }

    public void apply(final Player player) {
        KitApplyEvent event = new KitApplyEvent(this, player);
        Cardinal.getInstance().getServer().getPluginManager().callEvent(event);
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
        if (health >= 1 && health <= 20) {
            if (force) player.setHealth(health);
            else if (player.getHealth() < health) player.setHealth(health);
        }
        try {
            if (force) player.setSaturation(saturation);
            else if (player.getSaturation() < saturation) player.setSaturation(saturation);
        } catch (IllegalArgumentException e) {
        }
        if (foodLevel >= 0 && foodLevel <= 20) {
            if (force) player.setFoodLevel(foodLevel);
            else if (player.getFoodLevel() < foodLevel) player.setFoodLevel(foodLevel);
        }
        player.setWalkSpeed(walkSpeed);
        player.setKnockbackReduction(knockback);
        if (fly != null) {
            player.setAllowFlight(fly.canFly());
            player.setFlying(fly.isFlying());
            player.setFlySpeed(fly.getFlySpeed());
        }
        for (KitItem item : this.items) {
            if (item.hasSlot()) {
                if (player.getInventory().getItem(item.getSlot()) == null || force) {
                    player.getInventory().setItem(item.getSlot(), item.getItem());
                } else {
                    player.getInventory().addItem(item.getItem());
                }
            } else {
                player.getInventory().addItem(item.getItem());
            }
        }
        for (KitArmor armor : this.armor) {
            switch (armor.getType()) {
                case HELMET:
                    player.getInventory().setHelmet(armor.getItem());
                    break;
                case CHESTPLATE:
                    player.getInventory().setChestplate(armor.getItem());
                    break;
                case LEGGINGS:
                    player.getInventory().setLeggings(armor.getItem());
                    break;
                case BOOTS:
                    player.getInventory().setBoots(armor.getItem());
                    break;
            }
        }
        for (final PotionEffect effect : effects) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(GameHandler.getGameHandler().getPlugin(), new Runnable() {
                @Override
                public void run() {
                    player.addPotionEffect(effect, true);
                    player.setPotionParticles(false);
                }
            }, 0);
        }
        player.setPotionParticles(potionParticles);
        for (KitBook book : this.books) {
            ItemStack bookStack = new ItemStack(Material.WRITTEN_BOOK);
            if (book.hasSlot()) {
                if (player.getInventory().getItem(book.getSlot()) == null || force) {
                    BookMeta meta = (BookMeta) bookStack.getItemMeta();
                    meta.setTitle(ChatColor.translateAlternateColorCodes('`', book.getTitle()));
                    meta.setAuthor(ChatColor.translateAlternateColorCodes('`', book.getAuthor()));
                    meta.setPages(book.getPages());
                    bookStack.setItemMeta(meta);
                    player.getInventory().setItem(book.getSlot(), bookStack);
                } else {
                    player.getInventory().addItem(bookStack);
                }
            } else {
                player.getInventory().addItem(bookStack);
            }
        }
    }


    public String getName() {
        return name;
    }

    public boolean isResetPearls() {
        return resetPearls;
    }
}
