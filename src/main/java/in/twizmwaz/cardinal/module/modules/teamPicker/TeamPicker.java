package in.twizmwaz.cardinal.module.modules.teamPicker;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.titleRespawn.TitleRespawn;
import in.twizmwaz.cardinal.util.Items;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
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

import java.util.Arrays;
import java.util.Collections;

public class TeamPicker implements Module {

    protected TeamPicker() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public static ItemStack getTeamPicker(String locale) {
        return Items.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(locale) : new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(locale)),
                Collections.singletonList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(locale)));
    }

    public static String getTeamPickerTitle(String locale) {
        return ChatColor.DARK_RED + new LocalizedChatMessage(ChatConstant.UI_TEAM_PICK).getMessage(locale);
    }

    public Inventory getTeamPicker(Player player) {
        int size = ((GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class).size() + (Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get().isObserver() ? 0 : 1 ) + 8) / 9) * 9;
        int classesSize = ((GameHandler.getGameHandler().getMatch().getModules().getModules(ClassModule.class).size() + 8) / 9) * 9;
        Inventory picker = Bukkit.createInventory(null, size + classesSize, getTeamPickerTitle(player.getLocale()));
        int item = 0;

        int maxPlayers = 0;
        int totalPlayers = 0;
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (!team.isObserver()) {
                maxPlayers += team.getMax();
                totalPlayers += team.size();
            }
        }
        ItemStack autoJoin = Items.createItem(Material.CHAINMAIL_HELMET, 1, (short) 0, ChatColor.GRAY + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_AUTO).getMessage(player.getLocale()), Arrays.asList((totalPlayers >= maxPlayers ? ChatColor.RED + "" : ChatColor.GREEN + "") + totalPlayers + ChatColor.GOLD + " / " + ChatColor.RED + "" + maxPlayers, ChatColor.AQUA + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_AUTO_LORE).getMessage(player.getLocale())));
        picker.setItem(item, autoJoin);
        item++;
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (!team.isObserver()) {
                ItemStack teamStack = Items.createLeatherArmor(Material.LEATHER_HELMET, 1, team.getColor() + "" + ChatColor.BOLD + team.getName(), Arrays.asList((team.size() >= team.getMax() ? ChatColor.RED + "" : ChatColor.GREEN + "") + team.size() + ChatColor.GOLD + " / " + ChatColor.RED + "" + team.getMax(), ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_TEAM_CAN_PICK).getMessage(player.getLocale())), MiscUtil.convertChatColorToColor(team.getColor()));
                if (Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get().equals(team)) teamStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                picker.setItem(item, teamStack);
                item++;
            }
        }
        item = size;
        if (!(Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get().isObserver()) || GameHandler.getGameHandler().getMatch().getModules().getModule(TitleRespawn.class).isDeadUUID(player.getUniqueId())){
            ItemStack leave = Items.createItem(Material.LEATHER_BOOTS, 1, (short) 0, ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_LEAVE).getMessage(player.getLocale()), Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_LEAVE_LORE).getMessage(player.getLocale())));
            picker.setItem(item - 1, leave);
        }
        for (ClassModule classModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ClassModule.class)) {
            ItemStack classStack = Items.createItem(classModule.getIcon(), 1, (short) 0, ChatColor.GREEN + classModule.getName(), Arrays.asList(ChatColor.GOLD + classModule.getLongDescription()));
            if (classModule.equals(ClassModule.getClassByPlayer(player))) {
                classStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
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
        if (item != null && event.getInventory().getName().equals(getTeamPickerTitle(player.getLocale())) &&
                ObserverModule.testObserverOrDead(player) && !item.isSimilar(getTeamPicker(player.getLocale())) &&
                item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            boolean action = false;
            if (item.getType().equals(Material.CHAINMAIL_HELMET) &&
                    item.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_AUTO).getMessage(player.getLocale()))) {
                action = true;
                try {
                    Teams.setPlayerTeam(player, "");
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + e.getMessage());
                }
            } else if (item.getType().equals(Material.LEATHER_BOOTS) &&
                    item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_LEAVE).getMessage(player.getLocale()))) {
                action = true;
                try {
                    Teams.setPlayerTeam(player, Teams.getTeamById("observers").get().getName());
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + e.getMessage());
                }
            } else if (item.getType().equals(Material.LEATHER_HELMET) &&
                Teams.getTeamByName(ChatColor.stripColor(item.getItemMeta().getDisplayName())) != null) {
                action = true;
                try {
                    Bukkit.getConsoleSender().sendMessage(player.getName() + " joined " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                    Teams.setPlayerTeam(player, ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + e.getMessage());
                }
            } else if (ClassModule.getClassByName(ChatColor.stripColor(item.getItemMeta().getDisplayName())) != null) {
                action = true;
                Bukkit.dispatchCommand(player, "class " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
            }
            if (action) {
                event.setCancelled(true);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Match match = GameHandler.getGameHandler().getMatch();
        if (!match.hasEnded() && !(Blitz.matchIsBlitz() && match.isRunning()) && ObserverModule.testObserverOrDead(event.getPlayer())
                && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                && event.getItem() != null && event.getItem().equals(getTeamPicker(event.getPlayer().getLocale()))) {
            event.setCancelled(true);
            event.getPlayer().openInventory(getTeamPicker(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerLocaleChange(PlayerLocaleChangeEvent event) {
        ItemStack oldItem = getTeamPicker(event.getOldLocale() != null ? event.getOldLocale() : "en_US");
        ItemStack newItem = getTeamPicker(event.getNewLocale());
        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            if (item != null && item.equals(oldItem)) {
                item.setItemMeta(newItem.getItemMeta());
            }
        }
    }

}
