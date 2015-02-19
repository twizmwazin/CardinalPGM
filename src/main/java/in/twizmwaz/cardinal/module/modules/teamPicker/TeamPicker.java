package in.twizmwaz.cardinal.module.modules.teamPicker;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.MiscUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;

public class TeamPicker implements Module {

    protected TeamPicker() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }


    public Inventory getTeamPicker(Player player) {
        int size = ((GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class).size() / 9) + 1) * 9;
        int classesSize = ((GameHandler.getGameHandler().getMatch().getModules().getModules(ClassModule.class).size() + 8) / 9) * 9;
        Inventory picker = Bukkit.createInventory(null, size + classesSize, ChatColor.DARK_RED + "Pick your team");
        int item = 0;
        ItemStack autoJoin = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemMeta autoJoinMeta = autoJoin.getItemMeta();
        autoJoinMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Auto Join");
        int maxPlayers = 0;
        int totalPlayers = 0;
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (!team.isObserver()) {
                maxPlayers += team.getMax();
                totalPlayers += team.size();
            }
        }
        autoJoinMeta.setLore(Arrays.asList((totalPlayers >= maxPlayers ? ChatColor.RED + "" : ChatColor.GREEN + "") + totalPlayers + ChatColor.GOLD + " / " + ChatColor.RED + "" + maxPlayers, ChatColor.AQUA + "Puts you on the team with the fewest players"));
        autoJoin.setItemMeta(autoJoinMeta);
        picker.setItem(item, autoJoin);
        item++;
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (!team.isObserver()) {
                ItemStack teamStack = new ItemStack(Material.LEATHER_HELMET);
                ItemMeta teamMeta = teamStack.getItemMeta();
                teamMeta.setDisplayName(team.getColor() + "" + ChatColor.BOLD + team.getName());
                teamMeta.setLore(Arrays.asList((team.size() >= team.getMax() ? ChatColor.RED + "" : ChatColor.GREEN + "") + team.size() + ChatColor.GOLD + " / " + ChatColor.RED + "" + team.getMax(), ChatColor.GREEN + "You are able to pick your team, click to join!"));
                teamStack.setItemMeta(teamMeta);
                LeatherArmorMeta teamLeatherMeta = (LeatherArmorMeta) teamStack.getItemMeta();
                teamLeatherMeta.setColor(MiscUtils.convertChatColorToColor(team.getColor()));
                teamStack.setItemMeta(teamLeatherMeta);
                picker.setItem(item, teamStack);
                item++;
            }
        }
        item = size;
        for (ClassModule classModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ClassModule.class)) {
            ItemStack classStack = new ItemStack(classModule.getIcon());
            ItemMeta classMeta = classStack.getItemMeta();
            classMeta.setDisplayName(ChatColor.GREEN + classModule.getName());
            classMeta.setLore(Arrays.asList(ChatColor.GOLD + classModule.getLongDescription()));
            classStack.setItemMeta(classMeta);
            if (classModule.equals(ClassModule.getClassByPlayer(player))) {
                classStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
            }
            picker.setItem(item, classStack);
            item++;
        }
        return picker;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item != null) {
            if (TeamUtils.getTeamByPlayer(player).isObserver() || !GameHandler.getGameHandler().getMatch().isRunning()) {
                if (event.getInventory().getName().equals(ChatColor.DARK_RED + "Pick your team")) {
                    if (item.getType().equals(Material.CHAINMAIL_HELMET)) {
                        if (item.hasItemMeta()) {
                            if (item.getItemMeta().hasDisplayName()) {
                                if (item.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "" + ChatColor.BOLD + "Auto Join")) {
                                    event.setCancelled(true);
                                    player.closeInventory();
                                    player.playSound(player.getLocation(), Sound.CLICK, 1, 2);
                                    Bukkit.dispatchCommand(player, "join");
                                }
                            }
                        }
                    } else if (item.getType().equals(Material.LEATHER_HELMET)) {
                        if (item.hasItemMeta()) {
                            if (item.getItemMeta().hasDisplayName()) {
                                if (TeamUtils.getTeamByName(ChatColor.stripColor(item.getItemMeta().getDisplayName())) != null) {
                                    event.setCancelled(true);
                                    player.closeInventory();
                                    player.playSound(player.getLocation(), Sound.CLICK, 1, 2);
                                    Bukkit.dispatchCommand(player, "join " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                                } else {
                                    event.setCancelled(true);
                                    player.closeInventory();
                                    player.playSound(player.getLocation(), Sound.CLICK, 1, 2);
                                    Bukkit.dispatchCommand(player, "class " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                                }
                            }
                        }
                    } else {
                        if (item.hasItemMeta()) {
                            if (item.getItemMeta().hasDisplayName()) {
                                if (ClassModule.getClassByName(ChatColor.stripColor(item.getItemMeta().getDisplayName())) != null) {
                                    event.setCancelled(true);
                                    player.closeInventory();
                                    player.playSound(player.getLocation(), Sound.CLICK, 1, 2);
                                    Bukkit.dispatchCommand(player, "class " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() || !GameHandler.getGameHandler().getMatch().isRunning()) {
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getPlayer().getItemInHand() != null) {
                    if (event.getPlayer().getItemInHand().getType().equals(Material.LEATHER_HELMET)) {
                        if (event.getPlayer().getItemInHand().hasItemMeta()) {
                            if (event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) {
                                if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(event.getPlayer().getLocale())) || ChatColor.stripColor(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()).equals(new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(event.getPlayer().getLocale()))) {
                                    event.getPlayer().openInventory(getTeamPicker(event.getPlayer()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLocaleChange(PlayerLocaleChangeEvent event) {
        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            if (item != null) {
                if (item.getType().equals(Material.LEATHER_HELMET)) {
                    if (item.hasItemMeta()) {
                        if (item.getItemMeta().hasDisplayName()) {
                            if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(event.getOldLocale()))) {
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(event.getNewLocale()));
                                meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(event.getPlayer().getLocale())));
                                item.setItemMeta(meta);
                            } else if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(event.getOldLocale()))) {
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(event.getNewLocale()));
                                meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(event.getPlayer().getLocale())));
                                item.setItemMeta(meta);
                            }
                        }
                    }
                }
            }
        }
    }
}
