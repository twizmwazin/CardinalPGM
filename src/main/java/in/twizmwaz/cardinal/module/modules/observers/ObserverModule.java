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
import in.twizmwaz.cardinal.util.Items;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang.WordUtils;
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
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
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
import org.bukkit.event.inventory.InventoryDragEvent;
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
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
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
        player.setFlying(true);
        player.setAffectsSpawning(false);
        player.setCollidesWithEntities(false);
        player.setCanPickupItems(false);

        if (clear) {
            player.getInventory().clear();
        }

        player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
        ItemStack howTo = Items.createBook(Material.WRITTEN_BOOK, 1, ChatColor.AQUA + "" + ChatColor.BOLD + "Coming Soon", ChatColor.GOLD + "CardinalPGM");
        player.getInventory().setItem(1, howTo);
        player.getInventory().setItem(3, Tutorial.getEmerald(player));
        if (player.hasPermission("tnt.defuse")) {
            ItemStack shears = Items.createItem(Material.SHEARS, 1, (short) 0, ChatColor.RED + new LocalizedChatMessage(ChatConstant.UI_TNT_DEFUSER).getMessage(player.getLocale()));
            player.getInventory().setItem(5, shears);
        }
        player.closeInventory();

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
                ItemStack picker = Items.createItem(Material.LEATHER_HELMET, 1, (short) 0,
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
            event.setCancelled(true);
            if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.WRITTEN_BOOK))){
                ((CraftHumanEntity) event.getPlayer()).getHandle().openBook(CraftItemStack.asNMSCopy(event.getPlayer().getItemInHand()));
            }
            if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
                    Inventory chest = Bukkit.createInventory(null, ((Chest) event.getClickedBlock().getState()).getInventory().getSize());
                    for (int i = 0; i < ((Chest) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        chest.setItem(i, ((Chest) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(chest);
                }
                if (event.getClickedBlock().getType().equals(Material.FURNACE) || event.getClickedBlock().getType().equals(Material.BURNING_FURNACE)) {
                    Inventory furnace = Bukkit.createInventory(null, InventoryType.FURNACE);
                    for (int i = 0; i < ((Furnace) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        furnace.setItem(i, ((Furnace) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(furnace);
                }
                if (event.getClickedBlock().getType().equals(Material.DISPENSER)) {
                    Inventory dispenser = Bukkit.createInventory(null, InventoryType.DISPENSER);
                    for (int i = 0; i < ((Dispenser) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        dispenser.setItem(i, ((Dispenser) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(dispenser);
                }
                if (event.getClickedBlock().getType().equals(Material.DROPPER)) {
                    Inventory dropper = Bukkit.createInventory(null, InventoryType.DROPPER);
                    for (int i = 0; i < ((Dropper) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        dropper.setItem(i, ((Dropper) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(dropper);
                }
                if (event.getClickedBlock().getType().equals(Material.HOPPER)) {
                    Inventory hopper = Bukkit.createInventory(null, InventoryType.HOPPER);
                    for (int i = 0; i < ((Hopper) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        hopper.setItem(i, ((Hopper) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(hopper);
                }
                if (event.getClickedBlock().getType().equals(Material.BREWING_STAND)) {
                    Inventory brewingStand = Bukkit.createInventory(null, InventoryType.BREWING);
                    for (int i = 0; i < ((BrewingStand) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        brewingStand.setItem(i, ((BrewingStand) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(brewingStand);
                }
                if (event.getClickedBlock().getType().equals(Material.BEACON)) {
                    Inventory beacon = Bukkit.createInventory(null, InventoryType.BEACON);
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
        if (event.getRightClicked() instanceof Player){
            openInventory(event.getPlayer(), (Player) event.getRightClicked(), false);
        } else if (event.getRightClicked() instanceof ItemFrame) {
            event.setCancelled(true);
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingInventoryDrag(InventoryDragEvent event) {
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
        closeInventories(event.getPlayer().getUniqueId());
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerChangeTeamEvent(PlayerChangeTeamEvent event) {
        if (event.getNewTeam().isPresent() && event.getNewTeam().get().isObserver()){
            closeInventories(event.getPlayer().getUniqueId());
        }
    }

    public void openInventory(Player viewer, Player view, boolean message){
        if (testObserver(viewer) && !testObserver(view)) {
            viewer.openInventory(getFakeInventory(view, viewer.getLocale()));
            if (!viewing.containsKey(view.getUniqueId())) {
                viewing.put(view.getUniqueId(), new ArrayList<UUID>());
            }
            viewing.get(view.getUniqueId()).add(viewer.getUniqueId());
        } else if (message){
            viewer.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_INVENTORY_NOT_VIEWABLE).getMessage(viewer.getLocale()));
        }
    }

    public void closeInventories (UUID uuid) {
        if (viewing.containsKey(uuid)) {
            for (int i = 0; i < viewing.get(uuid).size(); i++) {
                if (Bukkit.getPlayer(viewing.get(uuid).get(0)) != null) {
                    Bukkit.getPlayer(viewing.get(uuid).get(0)).closeInventory();
                }
            }
        }
    }

    public void refreshView(final UUID view) {
        if (Bukkit.getPlayer(view) != null && viewing.containsKey(view)) {
            List<UUID> viewers = viewing.get(view);
            for (int i = 0; i < viewers.size(); i++) {
                Player player = Bukkit.getPlayer(viewers.get(i));
                if (player != null && player.getOpenInventory().getTitle().contains(Bukkit.getPlayer(view).getName())) {
                    Inventory fake = getFakeInventory(Bukkit.getPlayer(view), player.getLocale());
                    for (int i2 = 0; i2 < 45; i2 ++) {
                        try {
                            player.getOpenInventory().setItem(i2, fake.getItem(i2));
                        } catch (NullPointerException e) {
                        }
                    }
                    if (!player.getOpenInventory().getTitle().equals(Bukkit.getPlayer(view).getDisplayName())){
                        player.openInventory(fake);
                        viewing.get(view).add(viewers.get(i));
                    }
                }
            }
        }
    }

    public Inventory getFakeInventory(Player player, String locale) {
        Inventory inventory = Bukkit.createInventory(null, 45, player.getDisplayName().length() > 32 ? Teams.getTeamColorByPlayer(player) + player.getName() : player.getDisplayName());
        inventory.setItem(0, player.getInventory().getHelmet());
        inventory.setItem(1, player.getInventory().getChestplate());
        inventory.setItem(2, player.getInventory().getLeggings());
        inventory.setItem(3, player.getInventory().getBoots());

        ItemStack potion;
        if (player.getActivePotionEffects().size() > 0){
            ArrayList<String> effects = new ArrayList<>();
            for (PotionEffect effect : player.getActivePotionEffects()) {
                String effectName = WordUtils.capitalizeFully(effect.getType().getName().toLowerCase().replaceAll("_", " "));
                effects.add(ChatColor.YELLOW + effectName + " " + (effect.getAmplifier() + 1));
            }
            potion = Items.createItem(Material.POTION, 1, (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_POTION_EFFECTS).getMessage(locale), effects);
        } else {
            potion = Items.createItem(Material.GLASS_BOTTLE, 1, (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_POTION_EFFECTS).getMessage(locale), new ArrayList<>(Collections.singletonList(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_NO_POTION_EFFECTS).getMessage(locale))));
        }
        inventory.setItem(6, potion);
        ItemStack food = Items.createItem(Material.COOKED_BEEF, player.getFoodLevel(), (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_HUNGER_LEVEL).getMessage(locale));
        inventory.setItem(7, food);
        ItemStack health = Items.createItem(Material.REDSTONE, (int) Math.ceil(player.getHealth()), (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_HEALTH_LEVEL).getMessage(locale));
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
            event.getItemDrop().remove();
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
                TeamModule teamModule = Teams.getTeamById("observers").get();
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
                TeamModule teamModule = Teams.getTeamById("observers").get();
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

    @EventHandler
    public void PlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (testObserver(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityCombustEvent(EntityCombustByBlockEvent event) {
        if (event.getEntity() instanceof Player && testObserver((Player)event.getEntity())){
            event.getEntity().setFireTicks(0);
        }
    }
    
    private boolean testObserver(Player player) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(player);
        return (team.isPresent() && team.get().isObserver()) || !match.isRunning();

    }

}
