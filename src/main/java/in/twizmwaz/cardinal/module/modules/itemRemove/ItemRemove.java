package in.twizmwaz.cardinal.module.modules.itemRemove;

import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemRemove implements Module {

    private final RemovedItem item;

    protected ItemRemove(RemovedItem item) {
        this.item = item;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        ItemStack itemStack = event.getEntity().getItemStack();
        if (itemStack.getType().equals(item.getMaterial()) && (itemStack.getDurability() == item.getData() || item.getData() < 0))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeathEvent(CardinalDeathEvent event) {
        List<ItemStack> toRemove = new ArrayList<>();
        for (ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType().equals(item.getMaterial()) && (itemStack.getDurability() == item.getData() || item.getData() < 0))
                toRemove.add(itemStack);
        }
        for (ItemStack itemStack : toRemove) {
            event.getPlayer().getInventory().remove(itemStack);
        }
    }

}
