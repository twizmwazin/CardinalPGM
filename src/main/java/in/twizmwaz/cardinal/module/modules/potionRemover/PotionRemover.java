package in.twizmwaz.cardinal.module.modules.potionRemover;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PotionRemover implements Module {

    protected PotionRemover() { }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType().equals(Material.POTION)) {
            final Player player = event.getPlayer();
            final int slot = player.getInventory().getHeldItemSlot();
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Cardinal.getInstance(), new Runnable() {
                public void run() {
                    player.getInventory().setItem(slot, null);
                }
            }, 1L);
        }
    }

}