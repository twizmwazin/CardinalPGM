package in.twizmwaz.cardinal.module.modules.deathMessages;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tracker.Cause;
import in.twizmwaz.cardinal.module.modules.tracker.Type;
import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.Teams;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DeathMessages implements Module {

    protected DeathMessages() {
    }

    @Override
    public void unload() {

    }

    BaseComponent item;

    public void formatItem (org.bukkit.inventory.ItemStack itemStack) {
        net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = new NBTTagCompound();
        nms.save(tag);
        BaseComponent finalItem = new TextComponent("");
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().getDisplayName() != null) {
            BaseComponent[] name = new TextComponent().fromLegacyText(itemStack.getItemMeta().getDisplayName());
            finalItem.addExtra(new TextComponent("["));
            for (BaseComponent component : name) {
                component.setItalic(true);
                finalItem.addExtra(component);
            }
            finalItem.addExtra(new TextComponent("]"));
        } else {
            finalItem = new TranslatableComponent(nms.a() + ".name");
            finalItem.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        }
        if  (!itemStack.getEnchantments().isEmpty()) {
            finalItem.setColor(net.md_5.bungee.api.ChatColor.AQUA);
        }
        finalItem.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(tag.toString())}));
        item = finalItem;
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        try {
            if (Teams.getTeamByPlayer(event.getPlayer()) != null) {
                String name = Teams.getTeamColorByPlayer(event.getPlayer()) + event.getPlayer().getName();
                LocalizedChatMessage deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_UNKNOWN, name + ChatColor.GRAY);
                DamageCause cause = event.getPlayer().getLastDamageCause().getCause();
                int fallDistance = Math.round(event.getPlayer().isInsideVehicle() ? event.getPlayer().getVehicle().getFallDistance() : event.getPlayer().getFallDistance());
                if (cause.equals(DamageCause.BLOCK_EXPLOSION) || cause.equals(DamageCause.ENTITY_EXPLOSION)) {
                    if (event.getKiller() != null) {
                        if (event.getKiller().equals(event.getPlayer())) {
                            deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_EXPLOSION_SELF,
                                    name + ChatColor.GRAY);
                        } else {
                            deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_EXPLOSION_PLAYER,
                                    name + ChatColor.GRAY,
                                    Teams.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY);
                        }
                    } else {
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_EXPLOSION,
                                name + ChatColor.GRAY);
                    }
                } else if (cause.equals(DamageCause.CONTACT)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_CONTACT,
                            name + ChatColor.GRAY);
                } else if (cause.equals(DamageCause.DROWNING)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_DROWNING,
                            name + ChatColor.GRAY);
                } else if (cause.equals(DamageCause.ENTITY_ATTACK)) {
                    if (event.getKiller() != null) {
                        if (event.getKiller().getItemInHand().getType().equals(Material.AIR)) {
                            deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_ATTACK_PLAYER_FISTS, name + ChatColor.GRAY, Teams.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY);
                        } else {
                            deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_ATTACK_PLAYER,
                                    name + ChatColor.GRAY,
                                    Teams.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY,
                                    "/item/");
                            formatItem(event.getKiller().getItemInHand());
                        }
                    } else {
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_ATTACK,
                                name + ChatColor.GRAY);
                    }
                } else if (cause.equals(DamageCause.FALL)) {
                    if (event.getTrackerDamageEvent() != null) {
                        TrackerDamageEvent damageEvent = event.getTrackerDamageEvent();
                        Player killer = event.getKiller();
                        if (damageEvent.getType().equals(Type.SHOT)) {
                            deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_FALL_PLAYER_SHOT,
                                    name + ChatColor.GRAY,
                                    (damageEvent.getDistance() == -1 ? "?" : damageEvent.getDistance() + ""),
                                    fallDistance + "",
                                    Teams.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY);
                        } else if (damageEvent.getType().equals(Type.KNOCKED)) {
                            deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_FALL_PLAYER_KNOCKED,
                                    new UnlocalizedChatMessage(name + ChatColor.GRAY),
                                    new UnlocalizedChatMessage(fallDistance + ""),
                                    new UnlocalizedChatMessage(Teams.getTeamColorByPlayer(killer) + killer.getName() + ChatColor.GRAY),
                                    (damageEvent.getItem().getType().equals(Material.AIR) ?
                                            new LocalizedChatMessage(ChatConstant.DEATH_FISTS) :
                                            new UnlocalizedChatMessage("/item/")));
                            formatItem(damageEvent.getItem());
                        } else if (damageEvent.getType().equals(Type.BLOWN)){
                            deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_FALL_PLAYER_BLOWN,
                                    name + ChatColor.GRAY,
                                    fallDistance + "",
                                    Teams.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY);
                        }
                    } else {
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_FALL,
                                name + ChatColor.GRAY,
                                fallDistance + "");
                    }
                } else if (cause.equals(DamageCause.FALLING_BLOCK)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_FALLING_BLOCK,
                            name + ChatColor.GRAY);
                } else if (cause.equals(DamageCause.FIRE) || cause.equals(DamageCause.FIRE_TICK)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_FIRE,
                            name + ChatColor.GRAY);
                } else if (cause.equals(DamageCause.LAVA)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_LAVA,
                            name + ChatColor.GRAY);
                } else if (cause.equals(DamageCause.LIGHTNING)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_LIGHTNING,
                            name + ChatColor.GRAY);
                } else if (cause.equals(DamageCause.MAGIC)) {
                    if (event.getKiller() != null) {
                        int distance = (int) Math.round(event.getPlayer().getLocation().distance(event.getKiller().getLocation()));
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_POTION_PLAYER,
                                name + ChatColor.GRAY,
                                Teams.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY,
                                distance + "");
                    } else {
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_POTION,
                                name + ChatColor.GRAY);
                    }
                } else if (cause.equals(DamageCause.PROJECTILE)) {
                    if (event.getKiller() != null) {
                        int distance = (int) Math.round(event.getPlayer().getLocation().distance(event.getKiller().getLocation()));
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_PROJECTILE_PLAYER,
                                name + ChatColor.GRAY,
                                Teams.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName() + ChatColor.GRAY,
                                distance + " ");
                    } else {
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_PROJECTILE,
                                name + ChatColor.GRAY);
                    }
                } else if (cause.equals(DamageCause.STARVATION)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_STARVATION,
                            name + ChatColor.GRAY);
                } else if (cause.equals(DamageCause.SUFFOCATION)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_SUFFOCATION,
                            name + ChatColor.GRAY);
                } else if (cause.equals(DamageCause.SUICIDE)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_SUICIDE,
                            name + ChatColor.GRAY);
                } else if (cause.equals(DamageCause.THORNS)) {
                    if (event.getKiller() != null) {
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_THORNS_PLAYER,
                                name + ChatColor.GRAY,
                                Teams.getTeamColorByPlayer(event.getKiller()) + event.getKiller().getName());
                    } else {
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_THORNS,
                                name + ChatColor.GRAY);
                    }
                } else if (cause.equals(DamageCause.VOID)) {
                    if (event.getTrackerDamageEvent() != null) {
                        TrackerDamageEvent damageEvent = event.getTrackerDamageEvent();
                        Player killer = event.getKiller();
                        if (damageEvent.getType().equals(Type.SHOT)) {
                            deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_VOID_PLAYER_SHOT,
                                    name + ChatColor.GRAY,
                                    damageEvent.getDistance() + "",
                                    Teams.getTeamColorByPlayer(killer) + killer.getName());
                        } else if (damageEvent.getType().equals(Type.KNOCKED)){
                            if (damageEvent.getItem().getType().equals(Material.AIR)){
                                deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_VOID_PLAYER_KNOCKED_FISTS,
                                        name + ChatColor.GRAY,
                                        Teams.getTeamColorByPlayer(killer) + killer.getName() + ChatColor.GRAY);
                            } else {
                                deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_VOID_PLAYER_KNOCKED,
                                        name + ChatColor.GRAY, Teams.getTeamColorByPlayer(killer) + killer.getName() + ChatColor.GRAY,
                                        "/item/");
                                formatItem(damageEvent.getItem());
                            }
                        } else if (damageEvent.getType().equals(Type.BLOWN)){
                            deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_VOID_PLAYER_BLOWN,
                                    name + ChatColor.GRAY,
                                    Teams.getTeamColorByPlayer(killer) + killer.getName() + ChatColor.GRAY);
                        }
                    } else {
                        deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_VOID,
                                name + ChatColor.GRAY);
                    }
                } else if (cause.equals(DamageCause.WITHER)) {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_WITHER,
                            name + ChatColor.GRAY);
                } else {
                    deathMessage = new LocalizedChatMessage(ChatConstant.DEATH_SUICIDE,
                            name + ChatColor.GRAY);
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    boolean involved = false;
                    if (event.getPlayer().equals(player)) {
                        involved = true;
                    } else if (event.getKiller() != null && event.getKiller().equals(player)) {
                        involved = true;
                    }
                    boolean white = false;
                    if (Settings.getSettingByName("HighlightDeathMessages") != null && Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("white") && involved) {
                        white = true;
                    }
                    if (Settings.getSettingByName("DeathMessages") == null || (Settings.getSettingByName("DeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("all") || (Settings.getSettingByName("DeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("own") && involved))) {
                        String toSendMsg = white ? deathMessage.getMessage(player.getLocale()).replaceAll(ChatColor.GRAY + "", ChatColor.WHITE + "") : deathMessage.getMessage(player.getLocale());
                        BaseComponent finalMessage = new TextComponent("");
                        if (toSendMsg.contains("/item/")) {
                            String[] splitMessage = toSendMsg.split("/", 3);
                            BaseComponent[] message;
                            message = new TextComponent().fromLegacyText(splitMessage[0]);
                            for (BaseComponent component : message) {
                                finalMessage.addExtra(component);
                            }
                            if (white && !item.toPlainText().contains("[") && !item.getColor().equals(net.md_5.bungee.api.ChatColor.AQUA)) {
                                item.setColor(net.md_5.bungee.api.ChatColor.WHITE);
                            }
                            finalMessage.addExtra(item);
                            if (splitMessage.length > 2) {
                                message = new TextComponent().fromLegacyText(splitMessage[2]);
                                for (BaseComponent component : message) {
                                    finalMessage.addExtra(component);
                                }
                            }
                        } else {
                            BaseComponent[] message;
                            message = new TextComponent().fromLegacyText(toSendMsg);
                            for (BaseComponent component : message) {
                                finalMessage.addExtra(component);
                            }
                        }
                        if (Settings.getSettingByName("HighlightDeathMessages") != null && !Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("white") && involved) {
                            if (Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("bold")) {
                                finalMessage.setBold(true);
                            } else if (Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("italics")) {
                                finalMessage.setItalic(true);
                            } else if (Settings.getSettingByName("HighlightDeathMessages").getValueByPlayer(player).getValue().equalsIgnoreCase("underline")) {
                                finalMessage.setUnderlined(true);
                            }
                        }
                        player.sendMessage(finalMessage);

                    }
                }
            }
        } catch (Exception e) {
        }
    }

}
