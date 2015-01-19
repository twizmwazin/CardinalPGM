package in.twizmwaz.cardinal.module.modules.deathMessages;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;


public class DeathMessages implements Module {

    protected DeathMessages() {
    }

    @Override
    public void unload() {

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (TeamUtils.getTeamByPlayer(event.getEntity()) != null) {
            String playerName = TeamUtils.getTeamByPlayer(event.getEntity()).getColor() + event.getEntity().getDisplayName();
            String deathMessage;
            DamageCause cause = event.getEntity().getLastDamageCause().getCause();
            if (cause.equals(DamageCause.BLOCK_EXPLOSION) || cause.equals(DamageCause.ENTITY_EXPLOSION)) {
                if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
                    if (TntTracker.getWhoPlaced(damageByEntityEvent.getDamager()) != null) {
                        if (Bukkit.getOfflinePlayer(TntTracker.getWhoPlaced(damageByEntityEvent.getDamager())).isOnline()) {
                            Player source = Bukkit.getPlayer(TntTracker.getWhoPlaced(damageByEntityEvent.getDamager()));
                            if (source.equals(event.getEntity())) {
                                deathMessage = playerName + ChatColor.GRAY + " 'sploded";
                            } else {
                                deathMessage = playerName + ChatColor.GRAY + " was blown up by " + TeamUtils.getTeamByPlayer(source).getColor() + source.getDisplayName() + ChatColor.GRAY + "'s TNT";
                            }
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " blew up";
                        }
                    } else {
                        deathMessage = playerName + ChatColor.GRAY + " blew up";
                    }
                } else {
                    deathMessage = playerName + ChatColor.GRAY + " blew up";
                }
            } else if (cause.equals(DamageCause.CONTACT)) {
                deathMessage = playerName + ChatColor.GRAY + " was pricked to death";
            } else if (cause.equals(DamageCause.DROWNING)) {
                deathMessage = playerName + ChatColor.GRAY + " forgot to breathe";
            } else if (cause.equals(DamageCause.ENTITY_ATTACK)) {
                if (event.getEntity().getKiller() != null) {
                    Player killer = event.getEntity().getKiller();
                    if (killer.getItemInHand().getType().equals(Material.AIR)) {
                        deathMessage = playerName + ChatColor.GRAY + " felt the fury of " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName() + ChatColor.GRAY + "'s fists";
                    } else {
                        deathMessage = playerName + ChatColor.GRAY + " was slain by " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName() + ChatColor.GRAY + "'s " + killer.getItemInHand().getType().name().replaceAll("_", " ").toLowerCase();
                    }
                } else {
                    deathMessage = playerName + ChatColor.GRAY + " was slain";
                }
            } else if (cause.equals(DamageCause.FALL)) {
                deathMessage = playerName + ChatColor.GRAY + " hit the ground too hard";
            } else if (cause.equals(DamageCause.FALLING_BLOCK)) {
                deathMessage = playerName + ChatColor.GRAY + " was squashed by a falling anvil";
            } else if (cause.equals(DamageCause.FIRE)) {
                deathMessage = playerName + ChatColor.GRAY + " burned to death";
            } else if (cause.equals(DamageCause.FIRE_TICK)) {
                deathMessage = playerName + ChatColor.GRAY + " burned to death";
            } else if (cause.equals(DamageCause.LAVA)) {
                deathMessage = playerName + ChatColor.GRAY + " tried to swim in lava";
            } else if (cause.equals(DamageCause.LIGHTNING)) {
                deathMessage = playerName + ChatColor.GRAY + " was struck by lightning";
            } else if (cause.equals(DamageCause.MAGIC)) {
                if (event.getEntity().getKiller() != null) {
                    Player killer = event.getEntity().getKiller();
                    int distance = (int) Math.round(event.getEntity().getLocation().distance(killer.getLocation()));
                    deathMessage = playerName + ChatColor.GRAY + " took " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName() + ChatColor.GRAY + "'s potion to the face (" + distance + " block" + (distance != 1 ? "s" : "") + ")";
                } else {
                    deathMessage = playerName + ChatColor.GRAY + " took a potion to the face";
                }
            } else if (cause.equals(DamageCause.PROJECTILE)) {
                if (event.getEntity().getKiller() != null) {
                    Player killer = event.getEntity().getKiller();
                    int distance = (int) Math.round(event.getEntity().getLocation().distance(killer.getLocation()));
                    deathMessage = playerName + ChatColor.GRAY + " was shot by " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName() + ChatColor.GRAY + " (" + distance + " block" + (distance != 1 ? "s" : "") + ")";
                } else {
                    deathMessage = playerName + ChatColor.GRAY + " was shot";
                }
            } else if (cause.equals(DamageCause.STARVATION)) {
                deathMessage = playerName + ChatColor.GRAY + " starved to death";
            } else if (cause.equals(DamageCause.SUFFOCATION)) {
                deathMessage = playerName + ChatColor.GRAY + " suffocated in a wall";
            } else if (cause.equals(DamageCause.SUICIDE)) {
                deathMessage = playerName + ChatColor.GRAY + " died";
            } else if (cause.equals(DamageCause.THORNS)) {
                if (event.getEntity().getKiller() != null) {
                    Player killer = event.getEntity().getKiller();
                    deathMessage = playerName + ChatColor.GRAY + " died trying to hurt " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName();
                } else {
                    deathMessage = playerName + ChatColor.GRAY + " died trying to hurt an enemy";
                }
            } else if (cause.equals(DamageCause.VOID)) {
                deathMessage = playerName + ChatColor.GRAY + " fell out of the world";
            } else if (cause.equals(DamageCause.WITHER)) {
                deathMessage = playerName + ChatColor.GRAY + " withered away";
            } else {
                deathMessage = playerName + ChatColor.GRAY + " died";
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(deathMessage);
            }
        }
        event.setDeathMessage(null);
    }

}
