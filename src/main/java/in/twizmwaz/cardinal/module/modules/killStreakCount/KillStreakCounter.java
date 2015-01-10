package in.twizmwaz.cardinal.module.modules.killStreakCount;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class KillStreakCounter implements Module {

    private final JavaPlugin plugin;

    protected KillStreakCounter(final Match match) {
        this.plugin = GameHandler.getGameHandler().getPlugin();
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
                event.getEntity().removeMetadata("killstreak", plugin);
                event.getEntity().setMetadata("killstreak", new LazyMetadataValue(plugin, LazyMetadataValue.CacheStrategy.NEVER_CACHE, new KillStreak(old + 1)));
            }
        }
    }

    @EventHandler
    public void onPgmSpawn(PgmSpawnEvent event) {
        try {
            event.getPlayer().removeMetadata("killstreak", plugin);
        } catch (NullPointerException e) {

        }
        event.getPlayer().setMetadata("killstreak", new LazyMetadataValue(plugin, LazyMetadataValue.CacheStrategy.NEVER_CACHE, new KillStreak(0)));
    }

}
