package in.twizmwaz.cardinal.module.modules.itemKeep;

import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemKeep implements Module {

    private final Material type;
    private final int damageValue;

    private Map<Player, Map<Integer, ItemStack>> items;

    protected ItemKeep(Material type, int damageValue) {
        this.type = type;
        this.damageValue = damageValue;

        this.items = new HashMap<>();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerDeath(CardinalDeathEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        Map<Integer, ItemStack> itemsToKeep = new HashMap<>();
        if (inventory.getContents() != null) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) != null) {
                    ItemStack item = inventory.getItem(i);
                    if (item.getType().equals(type) && item.getDurability() == damageValue) {
                        itemsToKeep.put(i, item);
                        inventory.clear(i);
                    }
                }
            }
        }
        items.put(player, itemsToKeep);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPgmSpawn(CardinalSpawnEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        if (items.containsKey(player)) {
            if (items.get(player) != null) {
                for (int i : items.get(player).keySet()) {
                    if (items.get(player).get(i) != null) {
                        inventory.setItem(i, items.get(player).get(i));
                    }
                }
            }
            items.remove(player);
        }
    }

}