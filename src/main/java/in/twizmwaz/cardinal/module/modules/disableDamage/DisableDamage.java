package in.twizmwaz.cardinal.module.modules.disableDamage;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Set;
import java.util.UUID;

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
                if (event.getEntity() instanceof Player && TntTracker.getWhoPlaced(((EntityDamageByEntityEvent) event).getDamager()) != null) {
                    Player player = (Player) event.getEntity();
                    UUID source = TntTracker.getWhoPlaced(((EntityDamageByEntityEvent) event).getDamager());
                    Match match = GameHandler.getGameHandler().getMatch();
                    if (Bukkit.getOfflinePlayer(source).isOnline()) {
                        if (Bukkit.getPlayer(source).equals(player)) {
                            event.setCancelled(!blockExplosionSelf);
                        } else if (TeamUtils.getTeamByPlayer(Bukkit.getPlayer(source)) == TeamUtils.getTeamByPlayer(player)) {
                            event.setCancelled(!blockExplosionAlly);
                        } else if (TeamUtils.getTeamByPlayer(Bukkit.getPlayer(source)) != TeamUtils.getTeamByPlayer(player)) {
                            event.setCancelled(!blockExplosionEnemy);
                        } else {
                            event.setCancelled(!blockExplosionOther);
                        }
                    }
                }
            } else if (damageTypes.contains(DamageCause.BLOCK_EXPLOSION)) event.setCancelled(true);
        }
    }
}