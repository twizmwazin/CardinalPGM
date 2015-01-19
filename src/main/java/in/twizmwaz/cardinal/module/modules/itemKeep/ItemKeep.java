package in.twizmwaz.cardinal.module.modules.itemKeep;

import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemKeep implements Module{

    private final KeptItem item;

    protected ItemKeep(KeptItem item){
        this.item = item;
    }
    private HashMap<Player, ItemStack[]> items = new HashMap<>();

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = event.getEntity();
            Inventory playerInventory = player.getInventory();
            ArrayList<ItemStack> itemsToKeep = new ArrayList<>();
            if(playerInventory.getContents() != null) {
                for (ItemStack stack : playerInventory.getContents()) {
                    if (stack != null) {
                        if(item.getMaterials() != null) {
                            if (item.getMaterials().contains(stack.getType())) {
                                stack.setDurability(item.getData());
                                itemsToKeep.add(stack);
                            }
                        }
                    }
                }
            }
           ItemStack[] itemstack = itemsToKeep.toArray(new ItemStack[itemsToKeep.size()]);
           items.put(player, itemstack);
        }
    }

    @EventHandler
    public void onPgmSpawn(PgmSpawnEvent event){
        Player player = event.getPlayer();
        Inventory inv = player.getInventory();
        if(items.get(player) != null) {
            for (ItemStack stack : items.get(player)) {
                if (stack != null) {
                    inv.addItem(stack);
                }
            }
        }
        items.remove(player);
    }

}
