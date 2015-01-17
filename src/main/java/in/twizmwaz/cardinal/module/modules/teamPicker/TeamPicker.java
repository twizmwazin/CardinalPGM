package in.twizmwaz.cardinal.module.modules.teamPicker;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.MiscUtils;
import in.twizmwaz.cardinal.util.TeamUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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


    public Inventory getTeamPicker() {
        int size = (((GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class).size() + 1) / 9) + 1) * 9;
        Inventory picker = Bukkit.createInventory(null, size, ChatColor.DARK_RED + "Pick your team");
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
        return picker;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item != null) {
            if (TeamUtil.getTeamByPlayer(player).isObserver() || !GameHandler.getGameHandler().getMatch().isRunning()) {
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
                                if (TeamUtil.getTeamByName(ChatColor.stripColor(item.getItemMeta().getDisplayName())) != null) {
                                    event.setCancelled(true);
                                    player.closeInventory();
                                    player.playSound(player.getLocation(), Sound.CLICK, 1, 2);
                                    Bukkit.dispatchCommand(player, "join " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
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
        if (TeamUtil.getTeamByPlayer(event.getPlayer()).isObserver() || !GameHandler.getGameHandler().getMatch().isRunning()) {
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getPlayer().getItemInHand() != null) {
                    if (event.getPlayer().getItemInHand().getType().equals(Material.LEATHER_HELMET)) {
                        if (event.getPlayer().getItemInHand().hasItemMeta()) {
                            if (event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) {
                                if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "Team Selection")) {
                                    event.getPlayer().openInventory(getTeamPicker());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
