package in.twizmwaz.cardinal.module.modules.observers;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.titleRespawn.TitleRespawn;
import in.twizmwaz.cardinal.rank.Rank;
import in.twizmwaz.cardinal.util.Items;
import in.twizmwaz.cardinal.util.Players;
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
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.event.entity.PotionEffectExpireEvent;
import org.bukkit.event.entity.PotionEffectRemoveEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAttackEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ObserverModule implements Module {

    private final Match match;
    private Map<UUID, List<UUID>> viewing = new HashMap<>();

    protected ObserverModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        if (Blitz.matchIsBlitz()){
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (testObserver(player)) {
                    player.getInventory().setItem(2, null);
                }
            }
        }
    }

    @EventHandler
    public void onCardinalSpawn(CardinalSpawnEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning() && !event.getTeam().isObserver()) {
            Players.resetPlayer(event.getPlayer());
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
            Players.canInteract(event.getPlayer(), true);
        } else {
            Players.setObserver(event.getPlayer());
        }
    }

    @EventHandler
    public void onBlockChange(BlockPlaceEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockChange(BlockBreakEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        if (testObserver(event.getPlayer())) {
            event.setCancelled(true);
            if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (event.getPlayer().getInventory().getItemInMainHand() != null && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WRITTEN_BOOK))){
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.ALLOW);
            }
            if (event.getClickedBlock() != null && !event.getPlayer().isSneaking() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (testObserver(event.getPlayer())) {
            if (event.getRightClicked() instanceof Player && !event.getPlayer().isSneaking()){
                if (event.getHand().equals(EquipmentSlot.HAND)) openInventory(event.getPlayer(), (Player) event.getRightClicked(), false);
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
    public void onViewingInventoryClick(CraftItemEvent event) {
        updateNextTick(event.getActor());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingInventoryMoveItem(InventoryClickEvent event) {
        updateNextTick(event.getActor());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingInventoryClick(InventoryCreativeEvent event) {
        updateNextTick(event.getActor());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingInventoryMoveItem(InventoryDragEvent event) {
        updateNextTick(event.getActor());
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingEntityAddEffect(PotionEffectAddEvent event) {
        if (event.getEntity() instanceof Player) {
            updateNextTick((Player)event.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingEntityRemoveEffect(PotionEffectRemoveEvent event) {
        if (event.getEntity() instanceof Player) {
            updateNextTick((Player)event.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingEntityExpireEffect(PotionEffectExpireEvent event) {
        if (event.getEntity() instanceof Player) {
            updateNextTick((Player)event.getEntity());
        }
    }

    private void updateNextTick(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                refreshView(player.getUniqueId());
            }
        });
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
        inventory.setItem(4, player.getInventory().getItemInOffHand());

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
        if (event.getWhoClicked() instanceof Player && (testDead((Player) event.getWhoClicked()) || (testObserver((Player) event.getWhoClicked()) && !event.getInventory().getType().equals(InventoryType.PLAYER)))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupExperience(PlayerPickupExperienceEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (testObserverOrDead((Player) event.getDamager())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityAttack(PlayerAttackEntityEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getAttacker() instanceof Player && testObserverOrDead((Player) event.getAttacker())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player && testObserverOrDead((Player) event.getEntered())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player && testObserverOrDead((Player) event.getExited())) {
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
            if (testObserverOrDead((Player) event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerConnect(PlayerLoginEvent event) {
        if (Bukkit.getBanList(BanList.Type.NAME).isBanned(event.getPlayer().getName())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Bukkit.getBanList(BanList.Type.NAME).getBanEntry(event.getPlayer().getName()).getReason());
        } else if (Rank.whitelistBypass(event.getPlayer().getUniqueId())) {
            event.allow();
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            if (testObserverOrDead((Player) event.getRemover())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityCombustEvent(EntityCombustByBlockEvent event) {
        if (event.getEntity() instanceof Player && testObserver((Player)event.getEntity())){
            event.getEntity().setFireTicks(0);
        }
    }

    public static boolean testObserverOrDead(Player player) {
        return testObserver(player) || testDead(player);
    }

    public static boolean testObserver(Player player) {
        Optional<TeamModule> team = Teams.getTeamOrPlayerManagerByPlayer(player);
        return (team.isPresent() && team.get().isObserver() || !GameHandler.getGameHandler().getMatch().isRunning());
    }

    public static boolean testDead(Player player) {
        return GameHandler.getGameHandler().getMatch().isRunning() && GameHandler.getGameHandler().getMatch().getModules().getModule(TitleRespawn.class).isDeadUUID(player.getUniqueId());
    }

}
