package in.twizmwaz.cardinal.module.modules.deathMessages;

import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tracker.Cause;
import in.twizmwaz.cardinal.module.modules.tracker.Type;
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
                String name = TeamUtils.getTeamColorByPlayer(event.getPlayer()) + event.getPlayer().getName(), deathMessage;
                DamageCause cause = event.getPlayer().getLastDamageCause().getCause();
                int fallDistance = Math.round(event.getPlayer().isInsideVehicle() ? event.getPlayer().getVehicle().getFallDistance() : event.getPlayer().getFallDistance());
                if (cause.equals(DamageCause.BLOCK_EXPLOSION) || cause.equals(DamageCause.ENTITY_EXPLOSION)) {
                    if (event.getKiller() != null) {
                        if (event.getKiller().equals(event.getPlayer())) {
                            deathMessage = name + ChatColor.GRAY + " 'sploded";
                        } else {
                            deathMessage = name + ChatColor.GRAY + " was blown up by " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY + "'s TNT";
                        }
                    } else {
                        deathMessage = name + ChatColor.GRAY + " blew up";
                    }
                } else if (cause.equals(DamageCause.CONTACT)) {
                    deathMessage = name + ChatColor.GRAY + " was pricked to death";
                } else if (cause.equals(DamageCause.DROWNING)) {
                    deathMessage = name + ChatColor.GRAY + " forgot to breathe";
                } else if (cause.equals(DamageCause.ENTITY_ATTACK)) {
                    if (event.getKiller() != null) {
                        if (event.getKiller().getItemInHand().getType().equals(Material.AIR)) {
                            deathMessage = name + ChatColor.GRAY + " felt the fury of " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY + "'s fists";
                        } else {
                            deathMessage = name + ChatColor.GRAY + " was slain by " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY + "'s " + (event.getKiller().getItemInHand().getEnchantments() != null && event.getKiller().getItemInHand().getEnchantments().size() > 0 ? "enchanted " : "") + event.getKiller().getItemInHand().getType().name().replaceAll("_", " ").toLowerCase();
                        }
                    } else {
                        deathMessage = name + ChatColor.GRAY + " was slain";
                    }
                } else if (cause.equals(DamageCause.FALL)) {
                    if (event.getTrackerDamageEvent() != null) {
                        TrackerDamageEvent damageEvent = event.getTrackerDamageEvent();
                        Player killer = event.getKiller();
                        if (damageEvent.getType().equals(Type.SHOT)) {
                            deathMessage = name + ChatColor.GRAY + " was " + damageEvent.getType().name().toLowerCase() + (damageEvent.getDistance() == -1 ? "" : " (" + damageEvent.getDistance() + " blocks)") + " off a high place " + "(" + fallDistance + " blocks)" + " by " + TeamUtils.getTeamColorByPlayer(killer) + killer.getName();
                        } else {
                            deathMessage = name + ChatColor.GRAY + " was " + damageEvent.getType().name().toLowerCase() + " off a high place " + "(" + fallDistance + " blocks)" + " by " + TeamUtils.getTeamColorByPlayer(killer) + killer.getName() + ChatColor.GRAY + "'s " + (damageEvent.getItem().getType().equals(Material.AIR) ? "fists of fury" : (damageEvent.getItem().getEnchantments() != null && damageEvent.getItem().getEnchantments().size() > 0 ? "enchanted " : "") + damageEvent.getItem().getType().name().replaceAll("_", " ").toLowerCase());
                        }
                    } else {
                        deathMessage = name + ChatColor.GRAY + " hit the ground too hard (" + fallDistance + " blocks)";
                    }
                } else if (cause.equals(DamageCause.FALLING_BLOCK)) {
                    deathMessage = name + ChatColor.GRAY + " was squashed by a falling anvil";
                } else if (cause.equals(DamageCause.FIRE)) {
                    deathMessage = name + ChatColor.GRAY + " burned to death";
                } else if (cause.equals(DamageCause.FIRE_TICK)) {
                    deathMessage = name + ChatColor.GRAY + " burned to death";
                } else if (cause.equals(DamageCause.LAVA)) {
                    deathMessage = name + ChatColor.GRAY + " tried to swim in lava";
                } else if (cause.equals(DamageCause.LIGHTNING)) {
                    deathMessage = name + ChatColor.GRAY + " was struck by lightning";
                } else if (cause.equals(DamageCause.MAGIC)) {
                    if (event.getKiller() != null) {
                        int distance = (int) Math.round(event.getPlayer().getLocation().distance(event.getKiller().getLocation()));
                        deathMessage = name + ChatColor.GRAY + " took " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY + "'s potion to the face (" + distance + " blocks)";
                    } else {
                        deathMessage = name + ChatColor.GRAY + " took a potion to the face";
                    }
                } else if (cause.equals(DamageCause.PROJECTILE)) {
                    if (event.getKiller() != null) {
                        int distance = (int) Math.round(event.getPlayer().getLocation().distance(event.getKiller().getLocation()));
                        deathMessage = name + ChatColor.GRAY + " was shot by " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY + " (" + distance + " blocks)";
                    } else {
                        deathMessage = name + ChatColor.GRAY + " was shot";
                    }
                } else if (cause.equals(DamageCause.STARVATION)) {
                    deathMessage = name + ChatColor.GRAY + " starved to death";
                } else if (cause.equals(DamageCause.SUFFOCATION)) {
                    deathMessage = name + ChatColor.GRAY + " suffocated in a wall";
                } else if (cause.equals(DamageCause.SUICIDE)) {
                    deathMessage = name + ChatColor.GRAY + " died";
                } else if (cause.equals(DamageCause.THORNS)) {
                    if (event.getKiller() != null) {
                        deathMessage = name + ChatColor.GRAY + " died trying to hurt " + TeamUtils.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName();
                    } else {
                        deathMessage = name + ChatColor.GRAY + " died trying to hurt an enemy";
                    }
                } else if (cause.equals(DamageCause.VOID)) {
                    if (event.getTrackerDamageEvent() != null) {
                        TrackerDamageEvent damageEvent = event.getTrackerDamageEvent();
                        Player killer = event.getKiller();
                        if (damageEvent.getType().equals(Type.SHOT)) {
                            deathMessage = name + ChatColor.GRAY + " was " + damageEvent.getType().name().toLowerCase() + (damageEvent.getDistance() == -1 ? "" : " (" + damageEvent.getDistance() + " blocks)") + " out of the world by " + TeamUtils.getTeamColorByPlayer(killer) + killer.getName();
                        } else {
                            deathMessage = name + ChatColor.GRAY + " was " + damageEvent.getType().name().toLowerCase() + " out of the world by " + TeamUtils.getTeamColorByPlayer(killer) + killer.getName() + ChatColor.GRAY + "'s " + (damageEvent.getCause().equals(Cause.TNT) ? "TNT" : (damageEvent.getItem().getType().equals(Material.AIR) ? "fists of fury" : (damageEvent.getItem().getEnchantments() != null && damageEvent.getItem().getEnchantments().size() > 0 ? "enchanted " : "") + damageEvent.getItem().getType().name().replaceAll("_", " ").toLowerCase()));
                        }
                    } else {
                        deathMessage = name + ChatColor.GRAY + " fell out of the world";
                    }
                } else if (cause.equals(DamageCause.WITHER)) {
                    deathMessage = name + ChatColor.GRAY + " withered away";
                } else {
                    deathMessage = name + ChatColor.GRAY + " died";
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    boolean involved = false;
                    if (event.getPlayer().equals(player)) {
                        involved = true;
                    } else if (event.getKiller() != null && event.getKiller().equals(player)) {
                        involved = true;
                    }
                    UnlocalizedChatMessage toSend = new UnlocalizedChatMessage(deathMessage);
                    if (Settings.getSettingByName("HighlightDeathMessages") != null && involved) {
                        if (Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("bold")) {
                            String message = deathMessage;
                            boolean bold = false;
                            boolean color = false;
                            for (int i = 0; i < message.length(); i++) {
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
                            for (int i = 0; i < message.length(); i++) {
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
                            for (int i = 0; i < message.length(); i++) {
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
                    if (Settings.getSettingByName("DeathMessages") == null || (Settings.getSettingByName("DeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("all") || (Settings.getSettingByName("DeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("own") && involved))) {
                        player.sendMessage(toSend.getMessage(player.getLocale()));
                    }
                }
            }
        } catch (Exception e) {
        }
    }

}
