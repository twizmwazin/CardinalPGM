package in.twizmwaz.cardinal.module.modules.spectatorTools;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.PlayerSettingChangeEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.teamPicker.TeamPicker;
import in.twizmwaz.cardinal.settings.Setting;
import in.twizmwaz.cardinal.settings.SettingValue;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.Items;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAttackEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class SpectatorTools implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public static ItemStack getSpectatorItem(String locale) {
        return Items.createItem(Material.DIAMOND, 1, (short) 0,
                ChatColor.AQUA + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_SPECTATOR_TOOLS).getMessage(locale),
                Collections.singletonList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_SPECTATOR_TOOLS_LORE).getMessage(locale)));
    }

    public static String getSpectatorMenuTitle(String locale) {
        String title = ChatColor.DARK_AQUA + new LocalizedChatMessage(ChatConstant.UI_SPECTATOR_TOOLS).getMessage(locale);
        return title.length() > 32 ? title.substring(0, 32) : title;
    }

    public static String getTeamsMenuTitle(String locale) {
        String title = ChatColor.DARK_AQUA + new LocalizedChatMessage(ChatConstant.UI_TEAMS).getMessage(locale);
        return title.length() > 32 ? title.substring(0, 32) : title;
    }

    public static String getTeleportMenuTitle(String locale) {
        String title = ChatColor.DARK_AQUA + new LocalizedChatMessage(ChatConstant.UI_TELEPORT_TO_TEAM_MEMBER).getMessage(locale);
        return title.length() > 32 ? title.substring(0, 32) : title;
    }

    public static String getEffectsMenuTitle(String locale) {
        String title = ChatColor.DARK_AQUA + new LocalizedChatMessage(ChatConstant.UI_CHANGE_EFFECTS).getMessage(locale);
        return title.length() > 32 ? title.substring(0, 32) : title;
    }

    public static ItemStack getTeleportItem(String locale) {
        return Items.createItem(Material.ENDER_PEARL, 1, (short) 0,
                ChatColor.BLUE + new LocalizedChatMessage(ChatConstant.UI_TELEPORT_TO_TEAM_MEMBER).getMessage(locale),
                Collections.singletonList(new LocalizedChatMessage(ChatConstant.UI_TELEPORT_TO_TEAM_MEMBER_LORE).getMessage(locale)));
    }

    public static ItemStack getVisibilityItem(String locale) {
        return Items.createItem(Material.EYE_OF_ENDER, 1, (short) 0,
                ChatColor.BLUE + new LocalizedChatMessage(ChatConstant.UI_TOGGLE_OBSERVERS).getMessage(locale),
                Collections.singletonList(new LocalizedChatMessage(ChatConstant.UI_TOGGLE_OBSERVERS_LORE).getMessage(locale)));
    }

    public static ItemStack getElytraItem(String locale) {
        return Items.createItem(Material.ELYTRA, 1, (short) 0,
                ChatColor.BLUE + new LocalizedChatMessage(ChatConstant.UI_TOGGLE_ELYTRA).getMessage(locale),
                Collections.singletonList(new LocalizedChatMessage(ChatConstant.UI_TOGGLE_ELYTRA_LORE).getMessage(locale)));
    }

    public static ItemStack getEffectsItem(String locale) {
        return Items.createItem(Material.EXP_BOTTLE, 1, (short) 0,
                ChatColor.BLUE + new LocalizedChatMessage(ChatConstant.UI_CHANGE_EFFECTS).getMessage(locale),
                Collections.singletonList(new LocalizedChatMessage(ChatConstant.UI_CHANGE_EFFECTS_LORE).getMessage(locale)));
    }

    public static ItemStack getGamemodeItem(String locale) {
        return Items.createItem(Material.LEVER, 1, (short) 0,
                ChatColor.BLUE + new LocalizedChatMessage(ChatConstant.UI_TOGGLE_GAMEMODE).getMessage(locale),
                Collections.singletonList(new LocalizedChatMessage(ChatConstant.UI_TOGGLE_GAMEMODE_LORE).getMessage(locale)));
    }

    public static ItemStack getSpeedItem(String locale, String value) {
        return Items.createItem(Material.SUGAR, 1, (short) 0,
                new LocalizedChatMessage(ChatConstant.UI_TOGGLE_SPEED, value).getMessage(locale),
                Collections.singletonList(new LocalizedChatMessage(ChatConstant.UI_TOGGLE_SPEED_LORE).getMessage(locale)));
    }

    public static ItemStack getNightVisionItem(String locale) {
        return Items.createItem(Material.GOLDEN_CARROT, 1, (short) 0,
                ChatColor.BLUE + new LocalizedChatMessage(ChatConstant.UI_TOGGLE_NIGHT_VISION).getMessage(locale),
                Collections.singletonList(new LocalizedChatMessage(ChatConstant.UI_TOGGLE_NIGHT_VISION_LORE).getMessage(locale)));
    }

    public static ItemStack getGoBackItem(String locale) {
        return Items.createItem(Material.BARRIER, 1, (short) 0,
                ChatColor.DARK_RED + new LocalizedChatMessage(ChatConstant.UI_GO_BACK).getMessage(locale),
                Collections.singletonList(new LocalizedChatMessage(ChatConstant.UI_GO_BACK_LORE).getMessage(locale)));
    }

    public Inventory getSpectatorMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, getSpectatorMenuTitle(player.getLocale()));
        inventory.setItem(0, getTeleportItem(player.getLocale()));
        inventory.setItem(2, getVisibilityItem(player.getLocale()));
        inventory.setItem(4, getElytraItem(player.getLocale()));
        inventory.setItem(6, getEffectsItem(player.getLocale()));
        inventory.setItem(8, getGamemodeItem(player.getLocale()));
        return inventory;
    }

    public Inventory getTeamsMenu(Player player) {
        int size = ((GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class).size() + 8) / 9) * 9;
        Inventory inventory = Bukkit.createInventory(null, size, getTeamsMenuTitle(player.getLocale()));
        int item = 0;

        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (!team.isObserver()) {
                inventory.setItem(item, Items.createLeatherArmor(Material.LEATHER_HELMET, 1, team.getCompleteName() + " " + parsedTeamSize(team), Collections.<String>emptyList(), MiscUtil.convertChatColorToColor(team.getColor())));
                item++;
            }
        }
        inventory.setItem(size - 1, getGoBackItem(player.getLocale()));
        return inventory;
    }

    private String parsedTeamSize(TeamModule team) {
        return ChatColor.WHITE + "(" + (team.size() > 0 ? ChatColor.GREEN : ChatColor.RED) + team.size() + ChatColor.WHITE + ")";
    }

    public Inventory getTeleportMenu(Player player, TeamModule team) {
        int size = ((team.size() + 9) / 9) * 9;
        Inventory inventory = Bukkit.createInventory(null, size, getTeleportMenuTitle(player.getLocale()));
        int item = 0;
        for (Player view : (List<Player>) team) {
            ItemStack skull = Items.createItem(Material.SKULL_ITEM, 1, (short) 3, view.getDisplayName(), Collections.singletonList("Click to teleport"));
            SkullMeta meta = ((SkullMeta) skull.getItemMeta());
            meta.setOwner(view.getName(), view.getUniqueId(), view.getSkin());
            skull.setItemMeta(meta);
            inventory.setItem(item, skull);
            item++;
        }
        inventory.setItem(size - 1, getGoBackItem(player.getLocale()));
        return inventory;
    }

    public Inventory getEffectsMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, getEffectsMenuTitle(player.getLocale()));
        int item = 0;
        for (SettingValue value : Settings.getSettingByName("Speed").getValues()) {
            inventory.setItem(item, getSpeedItem(player.getLocale(), value.getValue()));
            item++;
        }
        inventory.setItem(6, getNightVisionItem(player.getLocale()));
        inventory.setItem(8, getGoBackItem(player.getLocale()));
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        String locale = player.getLocale();
        if (item == null) return;
        if (event.getInventory().getName().equals(getSpectatorMenuTitle(event.getActor().getLocale()))) {
            if (item.isSimilar(getTeleportItem(locale))) {
                player.openInventory(getTeamsMenu(player));
            } else if (item.isSimilar(getVisibilityItem(locale))) {
                Bukkit.dispatchCommand(player, "toggle obs");
                player.closeInventory();
            } else if (item.isSimilar(getElytraItem(locale))) {
                Bukkit.dispatchCommand(player, "toggle elytra");
                player.closeInventory();
            } else if (item.isSimilar(getEffectsItem(locale))) {
                player.openInventory(getEffectsMenu(player));
            } else if (item.isSimilar(getGamemodeItem(locale))) {
                player.setGameMode(player.getGameMode().equals(GameMode.CREATIVE) ? GameMode.SPECTATOR : GameMode.CREATIVE);
                if (player.getGameMode().equals(GameMode.CREATIVE)) Bukkit.dispatchCommand(player, "!");
                player.closeInventory();
            }
        } else if (event.getInventory().getName().equals(getTeamsMenuTitle(locale))) {
            if (item.isSimilar(getGoBackItem(locale))) {
                player.openInventory(getSpectatorMenu(player));
            } else if (item.getType().equals(Material.LEATHER_HELMET) && item.getItemMeta().hasDisplayName() && !item.isSimilar(TeamPicker.getTeamPicker(locale))){
                TeamModule team = Teams.getTeamByName(ChatColor.stripColor(Strings.removeLastWord(item.getItemMeta().getDisplayName()))).orNull();
                if (team != null) {
                    player.openInventory(getTeleportMenu(player, team));
                }
            }
        } else if (event.getInventory().getName().equals(getTeleportMenuTitle(locale))) {
            if (item.isSimilar(getGoBackItem(locale))) {
                player.openInventory(getTeamsMenu(player));
            } else if (item.getType().equals(Material.SKULL_ITEM) && item.getItemMeta().hasDisplayName()) {
                Player teleport = Bukkit.getPlayer(((SkullMeta) item.getItemMeta()).getOwner());
                if (teleport != null) {
                    player.teleport(teleport);
                    player.closeInventory();
                }
            }
        } else if (event.getInventory().getName().equals(getEffectsMenuTitle(locale))) {
            if (item.isSimilar(getGoBackItem(locale))) {
                player.openInventory(getSpectatorMenu(player));
            } else if (item.isSimilar(getNightVisionItem(player.getLocale()))) {
                if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                } else {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
                }
                player.closeInventory();
            } else if (item.getType().equals(Material.SUGAR) && item.getItemMeta().hasDisplayName()) {
                int value = event.getSlot();
                Setting setting = Settings.getSettingByName("Speed");
                Bukkit.dispatchCommand(player, "set speed " + setting.getValues().get(value).getValue());
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onSettingsChange(PlayerSettingChangeEvent event) {
        if (ObserverModule.testObserver(event.getPlayer())) {
            if (event.getSetting().equals(Settings.getSettingByName("Speed"))) {
                event.getPlayer().setFlySpeed(0.1f * Float.parseFloat(event.getNewValue().getValue()));
            } else if (event.getSetting().equals(Settings.getSettingByName("Elytra"))) {
                if (event.getNewValue().getValue().equals("on")) {
                    event.getPlayer().getInventory().setChestplate(new ItemStack(Material.ELYTRA));
                } else {
                    ItemStack chestplate = event.getPlayer().getInventory().getChestplate();
                    if (chestplate != null && chestplate.getType().equals(Material.ELYTRA)) {
                        event.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpectatorMount(PlayerAttackEntityEvent event) {
        if (event.getLeftClicked() instanceof Player
                &&ObserverModule.testObserver(event.getPlayer())
                && !ObserverModule.testObserver((Player) event.getLeftClicked())
                && event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        boolean action = event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK);
        if (ObserverModule.testObserver(event.getPlayer()) && action && event.getItem() != null &&
                event.getItem().isSimilar(getSpectatorItem(event.getPlayer().getLocale()))) {
            event.setCancelled(true);
            event.getPlayer().openInventory(getSpectatorMenu(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerLocaleChange(PlayerLocaleChangeEvent event) {
        ItemStack oldItem = getSpectatorItem(event.getOldLocale() != null ? event.getOldLocale() : "en_US");
        ItemStack newItem = getSpectatorItem(event.getNewLocale());
        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            if (item != null && item.isSimilar(oldItem)) {
                item.setItemMeta(newItem.getItemMeta());
            }
        }
    }

}
