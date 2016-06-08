package in.twizmwaz.cardinal.module.modules.armorKeep;

import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class ArmorKeep implements Module {

    private final Material type;
    private final int damageValue;

    private Map<Player, ItemStack> helmet;
    private Map<Player, ItemStack> chestplate;
    private Map<Player, ItemStack> leggings;
    private Map<Player, ItemStack> boots;

    protected ArmorKeep(Material type, int damageValue) {
        this.type = type;
        this.damageValue = damageValue;

        this.helmet = new HashMap<>();
        this.chestplate = new HashMap<>();
        this.leggings = new HashMap<>();
        this.boots = new HashMap<>();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
        helmet.clear();
        chestplate.clear();
        leggings.clear();
        boots.clear();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerInventory inventory = player.getInventory();
        Map<Integer, ItemStack> itemsToKeep = new HashMap<>();
        if (inventory.getHelmet() != null) {
            if (inventory.getHelmet().getType().equals(type) && inventory.getHelmet().getDurability() == damageValue) {
                helmet.put(player, inventory.getHelmet());
                inventory.setHelmet(null);
            }
        }
        if (inventory.getChestplate() != null) {
            if (inventory.getChestplate().getType().equals(type) && inventory.getChestplate().getDurability() == damageValue) {
                chestplate.put(player, inventory.getChestplate());
                inventory.setChestplate(null);
            }
        }
        if (inventory.getLeggings() != null) {
            if (inventory.getLeggings().getType().equals(type) && inventory.getLeggings().getDurability() == damageValue) {
                leggings.put(player, inventory.getLeggings());
                inventory.setLeggings(null);
            }
        }
        if (inventory.getBoots() != null) {
            if (inventory.getBoots().getType().equals(type) && inventory.getBoots().getDurability() == damageValue) {
                boots.put(player, inventory.getBoots());
                inventory.setBoots(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPgmSpawn(CardinalSpawnEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        if (helmet.containsKey(player)) {
            if (helmet.get(player) != null) {
                inventory.setHelmet(helmet.get(player));
            }
        }
        if (chestplate.containsKey(player)) {
            if (chestplate.get(player) != null) {
                inventory.setChestplate(chestplate.get(player));
            }
        }
        if (leggings.containsKey(player)) {
            if (leggings.get(player) != null) {
                inventory.setLeggings(leggings.get(player));
            }
        }
        if (boots.containsKey(player)) {
            if (boots.get(player) != null) {
                inventory.setBoots(boots.get(player));
            }
        }
    }

}