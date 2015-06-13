package in.twizmwaz.cardinal.module.modules.itemKeep;

import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ItemKeep implements Module {

    private final Material type;
    private final int damageValue;

    private HashMap<Player, HashMap<Integer, ItemStack>> items;

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
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Inventory inventory = player.getInventory();
        HashMap<Integer, ItemStack> itemsToKeep = new HashMap<>();
        if (inventory.getContents() != null) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) != null) {
                    ItemStack item = inventory.getItem(i);
                    if (item.getType().equals(type) && item.getDurability() == damageValue) {
                        itemsToKeep.put(i, item);
                    }
                }
            }
        }
        items.put(player, itemsToKeep);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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