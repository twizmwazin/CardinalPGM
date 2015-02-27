package in.twizmwaz.cardinal.module.modules.killStreakCount;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.LazyMetadataValue;

public class KillStreakCounter implements Module {


    protected KillStreakCounter() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerKill(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (((Player) event.getEntity()).getHealth() <= 0) {
                int old = event.getEntity().getMetadata("killstreak").get(0).asInt();
                event.getEntity().removeMetadata("killstreak", Cardinal.getInstance());
                event.getEntity().setMetadata("killstreak", new LazyMetadataValue(Cardinal.getInstance(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new KillStreak(old + 1)));
            }
        }
    }

    @EventHandler
    public void onPgmSpawn(CardinalSpawnEvent event) {
        try {
            event.getPlayer().removeMetadata("killstreak", Cardinal.getInstance());
        } catch (NullPointerException e) {

        }
        event.getPlayer().setMetadata("killstreak", new LazyMetadataValue(Cardinal.getInstance(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new KillStreak(0)));
    }

}
