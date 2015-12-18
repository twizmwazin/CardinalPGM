package in.twizmwaz.cardinal.module.modules.enderpearlReset;

import in.twizmwaz.cardinal.event.KitApplyEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class EnderpearlReset implements Module {

    private Map<UUID, Entity> thrownPearls = new HashMap<>();

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getType().equals(EntityType.ENDER_PEARL)) {
            if (event.getEntity().getShooter() instanceof Player) {
                Player shooter = (Player) event.getEntity().getShooter();
                thrownPearls.put(shooter.getUniqueId(), event.getEntity());
            }
        }
    }

    @EventHandler
    public void onApplyingKit(KitApplyEvent event) {
        if (event.getKit().isResetPearls()) {
            Iterator<Map.Entry<UUID, Entity>> iterator = thrownPearls.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<UUID, Entity> entry = iterator.next();
                if (entry.getKey().equals(event.getPlayer().getUniqueId())) {
                    if (!entry.getValue().isDead()) {
                        entry.getValue().remove();
                        iterator.remove();
                    }
                }
            }
        }
    }
}
