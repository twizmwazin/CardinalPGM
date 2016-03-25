package in.twizmwaz.cardinal.module.modules.killStreakCount;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.metadata.LazyMetadataValue;

public class KillStreakCounter implements Module {


    protected KillStreakCounter() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerKill(CardinalDeathEvent event) {
        if (event.getKiller() != null && event.getKiller().getHealth() > 0) {
            int old = event.getKiller().getMetadata("killstreak").get(0).asInt();
            event.getKiller().removeMetadata("killstreak", Cardinal.getInstance());
            event.getKiller().setMetadata("killstreak", new LazyMetadataValue(Cardinal.getInstance(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new KillStreak(old + 1)));
        }
    }

    @EventHandler
    public void onPgmSpawn(CardinalSpawnEvent event) {
        if (event.isCancelled()) return;
        try {
            event.getPlayer().removeMetadata("killstreak", Cardinal.getInstance());
        } catch (NullPointerException e) {
        }
        event.getPlayer().setMetadata("killstreak", new LazyMetadataValue(Cardinal.getInstance(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new KillStreak(0)));
    }

}
