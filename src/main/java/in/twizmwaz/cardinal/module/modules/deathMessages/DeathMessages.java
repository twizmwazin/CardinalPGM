package in.twizmwaz.cardinal.module.modules.deathMessages;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tracker.DamageTracker;
import in.twizmwaz.cardinal.module.modules.tracker.SpleefTracker;
import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;


public class DeathMessages implements Module {

    protected DeathMessages() {
    }

    @Override
    public void unload() {

    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        try {
            if (TeamUtils.getTeamByPlayer(event.getPlayer()) != null) {
                if (!event.getPlayer().hasMetadata("teamChange")) {
                    String playerName = TeamUtils.getTeamByPlayer(event.getPlayer()).getColor() + event.getPlayer().getDisplayName();
                    String deathMessage;
                    DamageCause cause = event.getPlayer().getLastDamageCause().getCause();
                    if (cause.equals(DamageCause.BLOCK_EXPLOSION) || cause.equals(DamageCause.ENTITY_EXPLOSION)) {
                        if (event.getKiller() != null) {
                            if (event.getKiller().equals(event.getPlayer())) {
                                deathMessage = playerName + ChatColor.GRAY + " 'sploded";
                            } else {
                                deathMessage = playerName + ChatColor.GRAY + " was blown up by " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getDisplayName() + ChatColor.GRAY + "'s TNT";
                            }
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " blew up";
                        }
                    } else if (cause.equals(DamageCause.CONTACT)) {
                        deathMessage = playerName + ChatColor.GRAY + " was pricked to death";
                    } else if (cause.equals(DamageCause.DROWNING)) {
                        deathMessage = playerName + ChatColor.GRAY + " forgot to breathe";
                    } else if (cause.equals(DamageCause.ENTITY_ATTACK)) {
                        if (event.getKiller() != null) {
                            if (event.getKiller().getItemInHand().getType().equals(Material.AIR)) {
                                deathMessage = playerName + ChatColor.GRAY + " felt the fury of " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getDisplayName() + ChatColor.GRAY + "'s fists";
                            } else {
                                deathMessage = playerName + ChatColor.GRAY + " was slain by " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getDisplayName() + ChatColor.GRAY + "'s " + (event.getKiller().getItemInHand().getEnchantments() != null && event.getKiller().getItemInHand().getEnchantments().size() > 0 ? "enchanted " : "") + event.getKiller().getItemInHand().getType().name().replaceAll("_", " ").toLowerCase();
                            }
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " was slain";
                        }
                    } else if (cause.equals(DamageCause.FALL)) {
                        if (event.getKiller() != null) {
                            try {
                                TrackerDamageEvent damageEvent = event.getTrackerDamageEvent();
                                Player killer = (Player) damageEvent.getDamager();
                                if (damageEvent.getDamageType().equals(DamageTracker.Type.SHOT)) {
                                    int distance = (int) Math.round(event.getPlayer().getLocation().distance(killer.getLocation()));
                                    deathMessage = playerName + ChatColor.GRAY + " was " + damageEvent.getDamageType().name().toLowerCase() + " (" + distance + " blocks) off a high place " + "(" + Math.round(event.getPlayer().getFallDistance()) + " blocks)" + " by " + TeamUtils.getTeamColorByPlayer(killer) + killer.getDisplayName();
                                } else {
                                    deathMessage = playerName + ChatColor.GRAY + " was " + damageEvent.getDamageType().name().toLowerCase() + " off a high place " + "(" + Math.round(event.getPlayer().getFallDistance()) + " blocks)" + " by " + TeamUtils.getTeamColorByPlayer(killer) + killer.getDisplayName() + ChatColor.GRAY + "'s " + (damageEvent.getDamagerItem().getType().equals(Material.AIR) ? "fists of fury" : (damageEvent.getDamagerItem().getEnchantments() != null && damageEvent.getDamagerItem().getEnchantments().size() > 0 ? "enchanted " : "") + damageEvent.getDamagerItem().getType().name().replaceAll("_", " ").toLowerCase());
                                }
                            } catch (NullPointerException e) {
                                deathMessage = playerName + ChatColor.GRAY + " hit the ground too hard (" + Math.round(event.getPlayer().getFallDistance()) + " blocks)";
                            }
                        } else if (event.getPlayerSpleefEvent() != null) {
                            deathMessage = playerName + ChatColor.GRAY + " was spleefed off a high place " + "(" + Math.round(event.getPlayer().getFallDistance()) + " blocks)" + " by " + TeamUtils.getTeamColorByPlayer(event.getPlayerSpleefEvent().getSpleefer()) + event.getPlayerSpleefEvent().getSpleefer().getName() + ChatColor.GRAY + "'s " + (event.getPlayerSpleefEvent().getSpleefType().equals(SpleefTracker.Type.TNT) ? "TNT" : (event.getPlayerSpleefEvent().getSpleeferItem().getType().equals(Material.AIR) ? "fists of fury" : (event.getPlayerSpleefEvent().getSpleeferItem().getEnchantments() != null && event.getPlayerSpleefEvent().getSpleeferItem().getEnchantments().size() > 0 ? "enchanted " : "") + event.getPlayerSpleefEvent().getSpleeferItem().getType().name().replaceAll("_", " ").toLowerCase()));
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " hit the ground too hard (" + Math.round(event.getPlayer().getFallDistance()) + " blocks)";
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
                        if (event.getKiller() != null) {
                            int distance = (int) Math.round(event.getPlayer().getLocation().distance(event.getKiller().getLocation()));
                            deathMessage = playerName + ChatColor.GRAY + " took " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getDisplayName() + ChatColor.GRAY + "'s potion to the face (" + distance + " block" + (distance != 1 ? "s" : "") + ")";
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " took a potion to the face";
                        }
                    } else if (cause.equals(DamageCause.PROJECTILE)) {
                        if (event.getKiller() != null) {
                            int distance = (int) Math.round(event.getPlayer().getLocation().distance(event.getKiller().getLocation()));
                            deathMessage = playerName + ChatColor.GRAY + " was shot by " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getDisplayName() + ChatColor.GRAY + " (" + distance + " block" + (distance != 1 ? "s" : "") + ")";
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
                        if (event.getKiller() != null) {
                            deathMessage = playerName + ChatColor.GRAY + " died trying to hurt " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getDisplayName();
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " died trying to hurt an enemy";
                        }
                    } else if (cause.equals(DamageCause.VOID)) {
                        if (event.getKiller() != null) {
                            try {
                                TrackerDamageEvent damageEvent = event.getTrackerDamageEvent();
                                Player killer = (Player) damageEvent.getDamager();
                                if (damageEvent.getDamageType().equals(DamageTracker.Type.SHOT)) {
                                    int distance = (int) Math.round(event.getPlayer().getLocation().distance(killer.getLocation()));
                                    deathMessage = playerName + ChatColor.GRAY + " was " + damageEvent.getDamageType().name().toLowerCase() + " (" + distance + " blocks) out of the world by " + TeamUtils.getTeamColorByPlayer(killer) + killer.getDisplayName();
                                } else {
                                    deathMessage = playerName + ChatColor.GRAY + " was " + damageEvent.getDamageType().name().toLowerCase() + " out of the world by " + TeamUtils.getTeamColorByPlayer(killer) + killer.getDisplayName() + ChatColor.GRAY + "'s " + (damageEvent.getDamagerItem().getType().equals(Material.AIR) ? "fists of fury" : (damageEvent.getDamagerItem().getEnchantments() != null && damageEvent.getDamagerItem().getEnchantments().size() > 0 ? "enchanted " : "") + damageEvent.getDamagerItem().getType().name().replaceAll("_", " ").toLowerCase());
                                }
                            } catch (NullPointerException e) {
                                deathMessage = playerName + ChatColor.GRAY + " fell out of the world";
                            }
                        } else if (event.getPlayerSpleefEvent() != null) {
                            deathMessage = playerName + ChatColor.GRAY + " was spleefed out of the world by " + TeamUtils.getTeamColorByPlayer(event.getPlayerSpleefEvent().getSpleefer()) + event.getPlayerSpleefEvent().getSpleefer().getName() + ChatColor.GRAY + "'s " + (event.getPlayerSpleefEvent().getSpleefType().equals(SpleefTracker.Type.TNT) ? "TNT" : (event.getPlayerSpleefEvent().getSpleeferItem().getType().equals(Material.AIR) ? "fists of fury" : (event.getPlayerSpleefEvent().getSpleeferItem().getEnchantments() != null && event.getPlayerSpleefEvent().getSpleeferItem().getEnchantments().size() > 0 ? "enchanted " : "") + event.getPlayerSpleefEvent().getSpleeferItem().getType().name().replaceAll("_", " ").toLowerCase()));
                        } else {
                            deathMessage = playerName + ChatColor.GRAY + " fell out of the world";
                        }
                    } else if (cause.equals(DamageCause.WITHER)) {
                        deathMessage = playerName + ChatColor.GRAY + " withered away";
                    } else {
                        deathMessage = playerName + ChatColor.GRAY + " died";
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        boolean involved = false;
                        if (event.getPlayer().equals(player)) {
                            involved = true;
                        } else if (event.getKiller() != null) {
                            if (event.getKiller().equals(player)) {
                                involved = true;
                            }
                        } else if (event.getPlayerSpleefEvent() != null) {
                            if (event.getPlayerSpleefEvent().getSpleefer().equals(player)) {
                                involved = true;
                            }
                        }
                        UnlocalizedChatMessage toSend = new UnlocalizedChatMessage(deathMessage);
                        if (Settings.getSettingByName("HighlightDeathMessages") != null && involved) {
                            if (Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("bold")) {
                                String message = deathMessage;
                                boolean bold = false;
                                boolean color = false;
                                for (int i = 0; i < message.length(); i ++) {
                                    if (message.charAt(i) == '§') {
                                        color = true;
                                    } else {
                                        if (bold) {
                                            message = message.substring(0, i) + ChatColor.BOLD + message.substring(i);
                                            bold = false;
                                        }
                                        if (color) {
                                            color = false;
                                            bold = true;
                                        }
                                    }
                                }
                                toSend = new UnlocalizedChatMessage(message);
                            }
                            if (Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("italics")) {
                                String message = deathMessage;
                                boolean italic = false;
                                boolean color = false;
                                for (int i = 0; i < message.length(); i ++) {
                                    if (message.charAt(i) == '§') {
                                        color = true;
                                    } else {
                                        if (italic) {
                                            message = message.substring(0, i) + ChatColor.ITALIC + message.substring(i);
                                            italic = false;
                                        }
                                        if (color) {
                                            color = false;
                                            italic = true;
                                        }
                                    }
                                }
                                toSend = new UnlocalizedChatMessage(message);
                            }
                            if (Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("white")) {
                                toSend = new UnlocalizedChatMessage(deathMessage.replaceAll(ChatColor.GRAY + "", ChatColor.WHITE + ""));
                            }
                            if (Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("underline")) {
                                String message = deathMessage;
                                boolean underline = false;
                                boolean color = false;
                                for (int i = 0; i < message.length(); i ++) {
                                    if (message.charAt(i) == '§') {
                                        color = true;
                                    } else {
                                        if (underline) {
                                            message = message.substring(0, i) + ChatColor.UNDERLINE + message.substring(i);
                                            underline = false;
                                        }
                                        if (color) {
                                            color = false;
                                            underline = true;
                                        }
                                    }
                                }
                                toSend = new UnlocalizedChatMessage(message);
                            }
                        }
                        if (Settings.getSettingByName("DeathMessages") == null || Settings.getSettingByName("DeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("all")) {
                            player.sendMessage(toSend.getMessage(player.getLocale()));
                        }
                    }
                } else {
                    event.getPlayer().removeMetadata("teamChange", GameHandler.getGameHandler().getPlugin());
                }
            }
        } catch (Exception e) {
        }
    }

}
