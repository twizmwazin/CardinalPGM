package in.twizmwaz.cardinal.module.modules.itemRemove;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.Set;

public class ItemRemove implements Module {

    private final Set<Material> materials;

    protected ItemRemove(final Set<Material> materials) {
        this.materials = materials;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (materials.contains(event.getEntity().getItemStack().getType())) event.setCancelled(true);
    }

}
