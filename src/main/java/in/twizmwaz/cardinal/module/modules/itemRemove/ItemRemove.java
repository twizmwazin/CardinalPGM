package in.twizmwaz.cardinal.module.modules.itemRemove;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

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

}
