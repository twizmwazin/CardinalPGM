package in.twizmwaz.cardinal.module.modules.observers;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;

public class ObserverModule implements Module {

    private final Match match;

    protected ObserverModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.CREATIVE);
            player.setAffectsSpawning(false);
            player.setCollidesWithEntities(false);
            player.setCanPickupItems(false);

            player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
            ItemStack howTo = new ItemStack(Material.WRITTEN_BOOK);
            ItemMeta howToMeta = howTo.getItemMeta();
            howToMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Coming Soon");
            howTo.setItemMeta(howToMeta);
            BookMeta howToBookMeta = (BookMeta) howTo.getItemMeta();
            howToBookMeta.setAuthor(ChatColor.GOLD + "CardinalPGM");
            howTo.setItemMeta(howToBookMeta);
            player.getInventory().setItem(1, howTo);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (match.getState().equals(MatchState.ENDED) || match.getState().equals(MatchState.CYCLING)) {
            if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                Player player = event.getPlayer();
                player.setGameMode(GameMode.CREATIVE);
                player.setAffectsSpawning(false);
                player.setCollidesWithEntities(false);
                player.setCanPickupItems(false);

                player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
                ItemStack howTo = new ItemStack(Material.WRITTEN_BOOK);
                ItemMeta howToMeta = howTo.getItemMeta();
                howToMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Coming Soon");
                howTo.setItemMeta(howToMeta);
                BookMeta howToBookMeta = (BookMeta) howTo.getItemMeta();
                howToBookMeta.setAuthor(ChatColor.GOLD + "CardinalPGM");
                howTo.setItemMeta(howToBookMeta);
                player.getInventory().setItem(1, howTo);
            }
        }
    }

    @EventHandler
    public void onPlayerSpawn(PgmSpawnEvent event) {
        if (!event.getTeam().isObserver()) {
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
            event.getPlayer().setAffectsSpawning(true);
            event.getPlayer().setCollidesWithEntities(true);
            event.getPlayer().setCanPickupItems(true);
        } else {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            event.getPlayer().setAffectsSpawning(false);
            event.getPlayer().setCollidesWithEntities(false);
            event.getPlayer().setCanPickupItems(false);

            event.getPlayer().getInventory().setItem(0, new ItemStack(Material.COMPASS));
            ItemStack howTo = new ItemStack(Material.WRITTEN_BOOK);
            ItemMeta howToMeta = howTo.getItemMeta();
            howToMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Coming Soon");
            howTo.setItemMeta(howToMeta);
            BookMeta howToBookMeta = (BookMeta) howTo.getItemMeta();
            howToBookMeta.setAuthor(ChatColor.GOLD + "CardinalPGM");
            howTo.setItemMeta(howToBookMeta);
            event.getPlayer().getInventory().setItem(1, howTo);
            if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
                ItemStack picker = new ItemStack(Material.LEATHER_HELMET);
                ItemMeta pickerMeta = picker.getItemMeta();
                pickerMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Team Selection");
                pickerMeta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Join the game!"));
                picker.setItemMeta(pickerMeta);
                event.getPlayer().getInventory().setItem(2, picker);
            }
        }
    }

    @EventHandler
    public void onBlockChange(BlockPlaceEvent event) {
        if (TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockChange(BlockBreakEvent event) {
        if (TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        if (TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
            if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
                    Inventory chest = Bukkit.createInventory(null, ((Chest) event.getClickedBlock().getState()).getInventory().getSize(), (((Chest) event.getClickedBlock().getState()).getInventory().getSize() == 54 ? "Large Chest" : "Chest"));
                    for (int i = 0; i < ((Chest) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        chest.setItem(i, ((Chest) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(chest);
                }
                if (event.getClickedBlock().getType().equals(Material.FURNACE)) {
                    Inventory furnace = Bukkit.createInventory(null, InventoryType.FURNACE, "Furnace");
                    for (int i = 0; i < ((Furnace) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        furnace.setItem(i, ((Furnace) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(furnace);
                }
                if (event.getClickedBlock().getType().equals(Material.DISPENSER)) {
                    Inventory dispenser = Bukkit.createInventory(null, InventoryType.DISPENSER, "Dispenser");
                    for (int i = 0; i < ((Dispenser) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        dispenser.setItem(i, ((Dispenser) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(dispenser);
                }
                if (event.getClickedBlock().getType().equals(Material.DROPPER)) {
                    Inventory dropper = Bukkit.createInventory(null, InventoryType.DROPPER, "Dropper");
                    for (int i = 0; i < ((Dropper) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        dropper.setItem(i, ((Dropper) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(dropper);
                }
                if (event.getClickedBlock().getType().equals(Material.HOPPER)) {
                    Inventory hopper = Bukkit.createInventory(null, InventoryType.HOPPER, "Hopper");
                    for (int i = 0; i < ((Hopper) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        hopper.setItem(i, ((Hopper) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(hopper);
                }
                if (event.getClickedBlock().getType().equals(Material.BREWING_STAND)) {
                    Inventory brewingStand = Bukkit.createInventory(null, InventoryType.BREWING, "Brewing Stand");
                    for (int i = 0; i < ((BrewingStand) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        brewingStand.setItem(i, ((BrewingStand) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(brewingStand);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEntityEvent event) {
        if (TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() || match.getState() != MatchState.PLAYING) {
            if (event.getRightClicked() instanceof Player) {
                Player viewing = (Player) event.getRightClicked();
                Inventory toView = Bukkit.createInventory(null, 45, TeamUtils.getTeamByPlayer(viewing).getColor() + ((Player) event.getRightClicked()).getName());
                toView.setItem(0, viewing.getInventory().getHelmet());
                toView.setItem(1, viewing.getInventory().getChestplate());
                toView.setItem(2, viewing.getInventory().getLeggings());
                toView.setItem(3, viewing.getInventory().getBoots());
                ItemStack potion = new ItemStack(Material.POTION, 1);
                ItemMeta potionMeta = potion.getItemMeta();
                potionMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.ITALIC + "Potion Effects");
                ArrayList<String> effects = new ArrayList<String>();
                for (PotionEffect effect : viewing.getActivePotionEffects()) {
                    String effectType = effect.getType().getName().toLowerCase().replaceAll("_", " ");
                    if (effectType.contains(" ")) {
                        String temp = "";
                        String[] parts = effectType.split(" ");
                        for (String part : parts) {
                            temp += Character.toUpperCase(part.charAt(0)) + part.substring(1) + " ";
                        }
                        temp = temp.trim();
                        effectType = temp;
                    } else {
                        String temp = "";
                        temp += Character.toUpperCase(effectType.charAt(0)) + effectType.substring(1);
                        effectType = temp;
                    }
                    effects.add(ChatColor.GRAY + effectType + " " + (effect.getAmplifier() + 1));
                }
                potionMeta.setLore(effects);
                potion.setItemMeta(potionMeta);
                toView.setItem(6, potion);
                ItemStack food = new ItemStack(Material.SPECKLED_MELON, viewing.getFoodLevel());
                ItemMeta foodMeta = food.getItemMeta();
                foodMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.ITALIC + "Hunger Level");
                food.setItemMeta(foodMeta);
                toView.setItem(7, food);
                ItemStack health = new ItemStack(Material.POTION, (int) Math.ceil(viewing.getHealth()), (short) 16389);
                ItemMeta healthMeta = health.getItemMeta();
                healthMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.ITALIC + "Health Level");
                health.setItemMeta(healthMeta);
                toView.setItem(8, health);
                for (int i = 36; i <= 44; i++) {
                    toView.setItem(i, viewing.getInventory().getItem(i - 36));
                }
                for (int i = 9; i <= 35; i++) {
                    toView.setItem(i, viewing.getInventory().getItem(i));
                }
                event.getPlayer().openInventory(toView);


            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            if (TeamUtils.getTeamByPlayer((Player) event.getWhoClicked()).isObserver() || match.getState() != MatchState.PLAYING) {
                if (event.getInventory().getType() != InventoryType.PLAYER) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPickupXP(PlayerPickupExperienceEvent event) {
        if (TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() || match.getState() != MatchState.PLAYING) {
            ItemStack dropped = event.getItemDrop().getItemStack();
            event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - dropped.getAmount());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (event.getNewTeam().isObserver() || match.getState() != MatchState.PLAYING) {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            event.getPlayer().setAffectsSpawning(false);
        } else {
            event.getPlayer().setAffectsSpawning(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (TeamUtils.getTeamByPlayer(event.getEntity()) == null) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        } else if (TeamUtils.getTeamByPlayer(event.getEntity()).isObserver()) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (TeamUtils.getTeamByPlayer((Player) event.getDamager()).isObserver()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (TeamUtils.getTeamById("observers").contains(event.getPlayer()) || match.getState() != MatchState.PLAYING) {
            if (event.getTo().getY() <= -64) {
                TeamModule teamModule = TeamUtils.getTeamById("observers");
                ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
                }
                event.getPlayer().teleport(modules.getRandom().getLocation());
            }
        }
    }

}
