package in.twizmwaz.cardinal.module.modules.disableDamage;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.Set;

public class DisableDamage implements Module {

    private final Set<DamageCause> damageTypes;
    private boolean blockExplosionAlly = true;
    private boolean blockExplosionSelf = true;
    private boolean blockExplosionEnemy = true;
    private boolean blockExplosionOther = true;

    protected DisableDamage(final Set<DamageCause> damageTypes) {
        this.damageTypes = damageTypes;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public void setBlockExplosionAlly(boolean to) {
        blockExplosionAlly = to;
    }

    public void setBlockExplosionSelf(boolean to) {
        blockExplosionSelf = to;
    }

    public void setBlockExplosionEnemy(boolean to) {
        blockExplosionEnemy = to;
    }

    public void setBlockExplosionOther(boolean to) {
        blockExplosionOther = to;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (damageTypes.contains(event.getCause()) && (event.getCause() != DamageCause.BLOCK_EXPLOSION || event.getCause() != DamageCause.ENTITY_EXPLOSION)) {
            event.setCancelled(true);
        } else if (event.getCause() == DamageCause.BLOCK_EXPLOSION || event.getCause() == DamageCause.ENTITY_EXPLOSION) {
            if (event instanceof EntityDamageByEntityEvent) {
                if (event.getEntity() instanceof Player) {
                    Player player = (Player) event.getEntity();
                    String source = TntTracker.getWhoPlaced(((EntityDamageByEntityEvent) event).getDamager());
                    Match match = GameHandler.getGameHandler().getMatch();
                    if (Bukkit.getOfflinePlayer(source).isOnline()) {
                        if (!blockExplosionSelf && source.equals(player.getName())) {
                            event.setCancelled(true);
                            return;
                        }
                        if (!blockExplosionAlly && match.getTeam(player) == match.getTeam(Bukkit.getPlayer(source)) && !source.equals(player.getName())) {
                            event.setCancelled(true);
                            return;
                        }
                        if (!blockExplosionEnemy && match.getTeam(player) != match.getTeam(Bukkit.getPlayer(source))) {
                            event.setCancelled(true);
                            return;
                        }
                        if (!blockExplosionOther) event.setCancelled(true);
                    }
                }
            } else if (damageTypes.contains(DamageCause.BLOCK_EXPLOSION)) event.setCancelled(true);
        }
    }
}