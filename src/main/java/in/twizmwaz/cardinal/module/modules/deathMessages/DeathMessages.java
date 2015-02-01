package in.twizmwaz.cardinal.module.modules.deathMessages;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.module.modules.tracker.DamageTracker;
import in.twizmwaz.cardinal.module.modules.tracker.SpleefTracker;
import in.twizmwaz.cardinal.module.modules.tracker.event.PlayerSpleefEvent;
import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
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
        try {
            if (TeamUtils.getTeamByPlayer(event.getEntity()) != null) {
                if (!event.getEntity().hasMetadata("teamChange")) {
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
                                deathMessage = playerName + ChatColor.GRAY + " was slain by " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName() + ChatColor.GRAY + "'s " + (killer.getItemInHand().getEnchantments() != null && killer.getItemInHand().getEnchantments().size() > 0 ? "enchanted " : "") + killer.getItemInHand().getType().name().replaceAll("_", " ").toLowerCase();
                            }
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " was slain";
                        }
                    } else if (cause.equals(DamageCause.FALL)) {
                        if (event.getEntity().getKiller() != null) {
                            try {
                                TrackerDamageEvent damageEvent = DamageTracker.getLastDamageEvent(event.getEntity());
                                Player killer = (Player) damageEvent.getDamager();
                                if (damageEvent.getDamageType().equals(DamageTracker.Type.SHOT)) {
                                    int distance = (int) Math.round(event.getEntity().getLocation().distance(killer.getLocation()));
                                    deathMessage = playerName + ChatColor.GRAY + " was " + damageEvent.getDamageType().name().toLowerCase() + " (" + distance + " blocks) off a high place " + "(" + Math.round(event.getEntity().getFallDistance()) + " blocks)" + " by " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName();
                                } else {
                                    deathMessage = playerName + ChatColor.GRAY + " was " + damageEvent.getDamageType().name().toLowerCase() + " off a high place " + "(" + Math.round(event.getEntity().getFallDistance()) + " blocks)" + " by " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName() + ChatColor.GRAY + "'s " + (damageEvent.getDamagerItem().getType().equals(Material.AIR) ? "fists of fury" : (damageEvent.getDamagerItem().getEnchantments() != null && damageEvent.getDamagerItem().getEnchantments().size() > 0 ? "enchanted " : "") + damageEvent.getDamagerItem().getType().name().replaceAll("_", " ").toLowerCase());
                                }
                            } catch (NullPointerException e) {
                                deathMessage = playerName + ChatColor.GRAY + " hit the ground too hard (" + Math.round(event.getEntity().getFallDistance()) + " blocks)";
                            }
                        } else if (SpleefTracker.getLastSpleefEvent(event.getEntity()) != null && (System.currentTimeMillis() - SpleefTracker.getLastSpleefEvent(event.getEntity()).getTime() <= 10000)) {
                            PlayerSpleefEvent spleefEvent = SpleefTracker.getLastSpleefEvent(event.getEntity());
                            deathMessage = playerName + ChatColor.GRAY + " was spleefed off a high place " + "(" + Math.round(event.getEntity().getFallDistance()) + " blocks)" + " by " + (spleefEvent.getSpleefer().isOnline() ? TeamUtils.getTeamByPlayer((Player) spleefEvent.getSpleefer()).getColor() : ChatColor.AQUA) + spleefEvent.getSpleefer().getName() + ChatColor.GRAY + "'s " + (spleefEvent.getSpleefType().equals(SpleefTracker.Type.TNT) ? "TNT" : (spleefEvent.getSpleeferItem().getType().equals(Material.AIR) ? "fists of fury" : (spleefEvent.getSpleeferItem().getEnchantments() != null && spleefEvent.getSpleeferItem().getEnchantments().size() > 0 ? "enchanted " : "") + spleefEvent.getSpleeferItem().getType().name().replaceAll("_", " ").toLowerCase()));
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " hit the ground too hard (" + Math.round(event.getEntity().getFallDistance()) + " blocks)";
                        }
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
                        if (event.getEntity().getKiller() != null) {
                            try {
                                TrackerDamageEvent damageEvent = DamageTracker.getLastDamageEvent(event.getEntity());
                                Player killer = (Player) damageEvent.getDamager();
                                if (damageEvent.getDamageType().equals(DamageTracker.Type.SHOT)) {
                                    int distance = (int) Math.round(event.getEntity().getLocation().distance(killer.getLocation()));
                                    deathMessage = playerName + ChatColor.GRAY + " was " + damageEvent.getDamageType().name().toLowerCase() + " (" + distance + " blocks) out of the world by " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName();
                                } else {
                                    deathMessage = playerName + ChatColor.GRAY + " was " + damageEvent.getDamageType().name().toLowerCase() + " out of the world by " + TeamUtils.getTeamByPlayer(killer).getColor() + killer.getDisplayName() + ChatColor.GRAY + "'s " + (damageEvent.getDamagerItem().getType().equals(Material.AIR) ? "fists of fury" : (damageEvent.getDamagerItem().getEnchantments() != null && damageEvent.getDamagerItem().getEnchantments().size() > 0 ? "enchanted " : "") + damageEvent.getDamagerItem().getType().name().replaceAll("_", " ").toLowerCase());
                                }
                            } catch (NullPointerException e) {
                                deathMessage = playerName + ChatColor.GRAY + " fell out of the world";
                            }
                        } else if (SpleefTracker.getLastSpleefEvent(event.getEntity()) != null && (System.currentTimeMillis() - SpleefTracker.getLastSpleefEvent(event.getEntity()).getTime() <= 10000)) {
                            PlayerSpleefEvent spleefEvent = SpleefTracker.getLastSpleefEvent(event.getEntity());
                            deathMessage = playerName + ChatColor.GRAY + " was spleefed out of the world by " + (spleefEvent.getSpleefer().isOnline() ? TeamUtils.getTeamByPlayer((Player) spleefEvent.getSpleefer()).getColor() : ChatColor.AQUA) + spleefEvent.getSpleefer().getName() + ChatColor.GRAY + "'s " + (spleefEvent.getSpleefType().equals(SpleefTracker.Type.TNT) ? "TNT" : (spleefEvent.getSpleeferItem().getType().equals(Material.AIR) ? "fists of fury" : (spleefEvent.getSpleeferItem().getEnchantments() != null && spleefEvent.getSpleeferItem().getEnchantments().size() > 0 ? "enchanted " : "") + spleefEvent.getSpleeferItem().getType().name().replaceAll("_", " ").toLowerCase()));
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " fell out of the world";
                        }
                    } else if (cause.equals(DamageCause.WITHER)) {
                        deathMessage = playerName + ChatColor.GRAY + " withered away";
                    } else {
                        deathMessage = playerName + ChatColor.GRAY + " died";
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(deathMessage);
                    }
                } else {
                    event.getEntity().removeMetadata("teamChange", GameHandler.getGameHandler().getPlugin());
                }
            }
        } catch (Exception e) {
        }
        event.setDeathMessage(null);
    }

}
