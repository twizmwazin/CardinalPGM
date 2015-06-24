package in.twizmwaz.cardinal.module.modules.observers;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.tutorial.Tutorial;
import in.twizmwaz.cardinal.util.ItemUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupExperienceEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ObserverModule implements Module {

    private final Match match;
    private HashMap<UUID, List<UUID>> viewing = new HashMap<>();

    protected ObserverModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    private void resetPlayer(Player player, boolean clear) {
        player.setGameMode(GameMode.CREATIVE);
        player.setAffectsSpawning(false);
        player.setCollidesWithEntities(false);
        player.setCanPickupItems(false);

        if (clear) {
            player.getInventory().clear();
        }

        player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
        ItemStack howTo = ItemUtils.createBook(Material.WRITTEN_BOOK, 1, ChatColor.AQUA + "" + ChatColor.BOLD + "Coming Soon", ChatColor.GOLD + "CardinalPGM");
        player.getInventory().setItem(1, howTo);
        player.getInventory().setItem(3, Tutorial.getEmerald(player));
        if (player.hasPermission("tnt.defuse")) {
            ItemStack shears = ItemUtils.createItem(Material.SHEARS, 1, (short) 0, ChatColor.RED + new LocalizedChatMessage(ChatConstant.UI_TNT_DEFUSER).getMessage(player.getLocale()));
            player.getInventory().setItem(5, shears);
        }

        try {
            player.updateInventory();
        } catch (NullPointerException ignored) {
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            resetPlayer(player, true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (match.getState().equals(MatchState.ENDED) || match.getState().equals(MatchState.CYCLING)) {
            if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                Player player = event.getPlayer();
                resetPlayer(player, true);
            }
        }
    }

    @EventHandler
    public void onPlayerSpawn(CardinalSpawnEvent event) {
        if (!event.getTeam().isObserver()) {
            if (match.isRunning()) {
                event.getPlayer().setGameMode(GameMode.SURVIVAL);
                event.getPlayer().setAffectsSpawning(true);
                event.getPlayer().setCollidesWithEntities(true);
                event.getPlayer().setCanPickupItems(true);
            }
        } else {
            resetPlayer(event.getPlayer(), false);
            if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
                ItemStack picker = ItemUtils.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                        ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(event.getPlayer().getLocale()) : new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(event.getPlayer().getLocale())),
                        Collections.singletonList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(event.getPlayer().getLocale())));
                event.getPlayer().getInventory().setItem(2, picker);
            }
        }
    }

    @EventHandler
    public void onBlockChange(BlockPlaceEvent event) {
        if (testObserver(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockChange(BlockBreakEvent event) {
        if (testObserver(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        if (testObserver(event.getPlayer())) {
            if (!(event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.WRITTEN_BOOK) && event.getPlayer().getItemInHand().hasItemMeta() && event.getPlayer().getItemInHand().getItemMeta().hasDisplayName() && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "" + ChatColor.BOLD + "Coming Soon"))) {
                event.setCancelled(true);
            }
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
                if (event.getClickedBlock().getType().equals(Material.BURNING_FURNACE)) {
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
                if (event.getClickedBlock().getType().equals(Material.BEACON)) {
                    Inventory beacon = Bukkit.createInventory(null, InventoryType.BEACON, "Beacon");
                    for (int i = 0; i < ((Beacon) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        beacon.setItem(i, ((Beacon) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(beacon);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (testObserver(event.getPlayer())) {
            if (event.getRightClicked() instanceof Player && testObserver((Player) event.getRightClicked())) {
                event.getPlayer().openInventory(getFakeInventory((Player) event.getRightClicked(), event.getPlayer().getLocale()));
                setViewing(event.getPlayer().getUniqueId(), event.getRightClicked().getUniqueId());
            } else if (event.getRightClicked() instanceof ItemFrame) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        for (UUID uuid : viewing.keySet()) {
            if (viewing.get(uuid).contains(event.getPlayer().getUniqueId())) {
                List<UUID> viewingList = viewing.get(uuid);
                viewingList.remove(event.getPlayer().getUniqueId());
                viewing.put(uuid, viewingList);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingInventoryClick(InventoryClickEvent event) {
        refreshView(event.getWhoClicked().getUniqueId());
    }

    @EventHandler
    public void onViewingPlayerPickupItem(PlayerPickupItemEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onViewingPlayerDropItem(PlayerDropItemEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onViewingFoodLevelChange(FoodLevelChangeEvent event) {
        refreshView(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void onViewingEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            refreshView(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onViewingEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            refreshView(event.getEntity().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onViewingPlayerRespawn(PlayerRespawnEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            refreshView(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (viewing.containsKey(event.getPlayer().getUniqueId())) {
            List<UUID> toClose = new ArrayList<>();
            for (UUID uuid : viewing.get(event.getPlayer().getUniqueId())) {
                if (Bukkit.getPlayer(uuid) != null) {
                    toClose.add(uuid);
                }
            }
            for (UUID uuid : toClose) {
                Bukkit.getPlayer(uuid).closeInventory();
            }
        }
    }

    public void setViewing(UUID uuid, UUID view) {
        if (!viewing.containsKey(view)) {
            viewing.put(view, new ArrayList<UUID>());
        }
        viewing.get(view).add(uuid);
    }

    public void refreshView(final UUID view) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(GameHandler.getGameHandler().getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getPlayer(view) != null && viewing.containsKey(view)) {
                    List<UUID> toOpen = new ArrayList<>();
                    for (UUID uuid : viewing.get(view)) {
                        if (Bukkit.getPlayer(uuid) != null) {
                            toOpen.add(uuid);
                        }
                    }
                    for (UUID uuid : toOpen) {
                        Bukkit.getPlayer(uuid).openInventory(getFakeInventory(Bukkit.getPlayer(view), Bukkit.getPlayer(uuid).getLocale()));
                        setViewing(uuid, view);
                    }
                }

                /* if (Bukkit.getPlayer(view) != null && viewing.containsKey(view)) {
                    for (UUID uuid : viewing.get(view)) {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player != null && player.getOpenInventory().getTitle().equals(TeamUtils.getTeamColorByPlayer(Bukkit.getPlayer(view)) + Bukkit.getPlayer(view).getName())) {
                            Inventory fake = getFakeInventory(Bukkit.getPlayer(view), player.getLocale());
                            for (int i = 0; i < 36; i ++) {
                                try {
                                    player.getOpenInventory().setItem(i, fake.getItem(i));
                                } catch (NullPointerException e) {
                                }
                            }
                        }
                    }
                } */
            }
        }, 0);
    }

    public Inventory getFakeInventory(Player player, String locale) {
        Inventory inventory = Bukkit.createInventory(null, 45, TeamUtils.getTeamColorByPlayer(player) + player.getName());
        inventory.setItem(0, player.getInventory().getHelmet());
        inventory.setItem(1, player.getInventory().getChestplate());
        inventory.setItem(2, player.getInventory().getLeggings());
        inventory.setItem(3, player.getInventory().getBoots());

        ArrayList<String> effects = new ArrayList<>();
        for (PotionEffect effect : player.getActivePotionEffects()) {
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
        ItemStack potion = ItemUtils.createItem(Material.POTION, 1, (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_POTION_EFFECTS).getMessage(locale), effects);
        inventory.setItem(6, potion);
        ItemStack food = ItemUtils.createItem(Material.SPECKLED_MELON, player.getFoodLevel(), (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_HUNGER_LEVEL).getMessage(locale));
        inventory.setItem(7, food);
        ItemStack health = ItemUtils.createItem(Material.POTION, (int) Math.ceil(player.getHealth()), (short) 16389, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_HEALTH_LEVEL).getMessage(locale));
        inventory.setItem(8, health);
        for (int i = 36; i <= 44; i++) {
            inventory.setItem(i, player.getInventory().getItem(i - 36));
        }
        for (int i = 9; i <= 35; i++) {
            inventory.setItem(i, player.getInventory().getItem(i));
        }
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && testObserver((Player) event.getWhoClicked())) {
            if (event.getInventory().getType() != InventoryType.PLAYER) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickupExperience(PlayerPickupExperienceEvent event) {
        if (testObserver(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (testObserver(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTeamChange(PlayerChangeTeamEvent event) {
        if (testObserver(event.getPlayer())) {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            event.getPlayer().setAffectsSpawning(false);
        } else {
            event.getPlayer().setAffectsSpawning(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (testObserver(event.getEntity())) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (testObserver((Player) event.getDamager())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getAttacker() instanceof Player && testObserver((Player) event.getAttacker())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player && testObserver((Player) event.getEntered())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player && testObserver((Player) event.getExited())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (testObserver(event.getPlayer())) {
            if (event.getTo().getY() <= -64) {
                TeamModule teamModule = TeamUtils.getTeamById("observers").get();
                ModuleCollection<SpawnModule> modules = new ModuleCollection<>();
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
                }
                event.setTo(modules.getRandom().getLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (testObserver(event.getPlayer())) {
            if (event.getTo().getY() <= -64) {
                TeamModule teamModule = TeamUtils.getTeamById("observers").get();
                ModuleCollection<SpawnModule> modules = new ModuleCollection<>();
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam() == teamModule) modules.add(spawnModule);
                }
                event.setTo(modules.getRandom().getLocation());
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (testObserver((Player) event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerConnect(PlayerLoginEvent event) {
        if (Bukkit.getBanList(BanList.Type.NAME).isBanned(event.getPlayer().getName())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Bukkit.getBanList(BanList.Type.NAME).getBanEntry(event.getPlayer().getName()).getReason());
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (testObserver(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (testObserver((Player) event.getRemover())) {
                event.setCancelled(true);
            }
        }
    }

    private boolean testObserver(Player player) {
        Optional<TeamModule> team = TeamUtils.getTeamByPlayer(player);
        return (team.isPresent() && team.get().isObserver()) || !match.isRunning();

    }

}
