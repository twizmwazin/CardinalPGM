package in.twizmwaz.cardinal.module.modules.doubleKillPatch;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DoubleKillPatch implements Module {

    private Set<UUID> players = new HashSet<>();

    protected DoubleKillPatch() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!players.contains(event.getEntity().getUniqueId())) {
            players.add(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        while (players.contains(event.getPlayer().getUniqueId())) {
            players.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (players.contains(event.getDamager().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
