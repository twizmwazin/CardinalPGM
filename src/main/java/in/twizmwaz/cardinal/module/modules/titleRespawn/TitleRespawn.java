package in.twizmwaz.cardinal.module.modules.titleRespawn;

import com.google.common.base.Optional;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.teamPicker.TeamPicker;
import in.twizmwaz.cardinal.module.modules.visibility.Visibility;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import in.twizmwaz.cardinal.util.Watchers;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumItemSlot;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutEntityDestroy;
import net.minecraft.server.PacketPlayOutEntityEquipment;
import net.minecraft.server.PacketPlayOutEntityStatus;
import net.minecraft.server.PacketPlayOutMount;
import net.minecraft.server.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.PacketPlayOutWorldBorder;
import net.minecraft.server.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TitleRespawn implements TaskedModule {

    private final int delay;
    private final boolean auto;
    private final boolean blackout;
    private final boolean spectate;
    private final boolean bed;
    private final String message;

    private DecimalFormat formatter = new DecimalFormat("0.0");

    private Set<UUID> hasLeftClicked = new HashSet<>();
    private Map<UUID, Long> deadPlayers = new HashMap<>();

    public TitleRespawn(int delay, boolean auto, boolean blackout, boolean spectate, boolean bed, String message) {
        this.delay = delay;
        this.auto = auto;
        this.blackout = blackout;
        this.spectate = spectate;
        this.bed = bed;
        this.message = message;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        getSubtitleLogic();
    }

    private boolean isAllowedToRespawn(UUID id) {
        double timeLeftToRespawn = this.delay - ((System.currentTimeMillis() - deadPlayers.get(id)) / 1000.0);
        return this.isDeadUUID(id) && timeLeftToRespawn <= 0;
    }

    public boolean isDeadUUID(UUID id) {
        return this.deadPlayers.containsKey(id);
    }

    private boolean hasLeftClicked(UUID id) {
        return hasLeftClicked.contains(id);
    }

    private void getSubtitleLogic() {
        List<UUID> deadClone = new ArrayList<>();
        for (UUID uuid : this.deadPlayers.keySet()) deadClone.add(uuid);
        for (UUID id : deadClone) {
            double timeLeftToRespawn = this.delay - ((System.currentTimeMillis() - deadPlayers.get(id)) / 1000.0);
            Player player = Bukkit.getPlayer(id);
            if (timeLeftToRespawn > 0) {
                if (hasLeftClicked(id)) {
                    player.setSubtitle(TextComponent.fromLegacyText(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_DEATH_RESPAWN_CONFIRMED_TIME, ChatColor.AQUA + formatter.format(timeLeftToRespawn).replace(",", ".") + ChatColor.GREEN).getMessage(player.getLocale())));
                } else {
                    player.setSubtitle(TextComponent.fromLegacyText(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_DEATH_RESPAWN_UNCONFIRMED_TIME, ChatColor.AQUA + formatter.format(timeLeftToRespawn).replace(",",".") + ChatColor.GREEN).getMessage(player.getLocale())));
                }
            } else if (isAllowedToRespawn(id)) {
                if (hasLeftClicked(id) && playerCanRespawn(player)) {
                    respawnPlayer(player, true);
                } else {
                    if (hasLeftClicked(id)) {
                        if (message == null) {
                            player.setSubtitle(TextComponent.fromLegacyText(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_DEATH_RESPAWN_CONFIRMED_WAITING).getMessage(player.getLocale())));
                        } else {
                            player.setSubtitle(getSubtitleMessage(player.getLocale()));
                        }
                    } else {
                        player.setSubtitle(TextComponent.fromLegacyText(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_DEATH_RESPAWN_UNCONFIRMED).getMessage(player.getLocale())));
                    }
                    if (player.getInventory().getItem(2) == null || !player.getInventory().getItem(2).getType().equals(Material.LEATHER_HELMET)) {
                        ItemStack picker = TeamPicker.getTeamPicker(player.getLocale());
                        player.getInventory().setItem(2, picker);
                    }
                }
            }
        }
    }

    private boolean playerCanRespawn(Player player) {
        TeamModule team = Teams.getTeamByPlayer(player).get();
        for (SpawnModule spawnModule : Teams.getSpawns(team)) {
            FilterModule filter = spawnModule.getFilter();
            if (filter == null || filter.evaluate(player).equals(FilterState.ALLOW)) return true;
        }
        return false;
    }

    private BaseComponent[] getSubtitleMessage(String locale) {
        JsonParser parser = new JsonParser();
        JsonObject object = translateJson(parser.parse(message).getAsJsonObject(), locale);
        BaseComponent[] components = ComponentSerializer.parse(object.toString());
        components[0].setColor(net.md_5.bungee.api.ChatColor.GREEN);
        return components;
    }

    private JsonObject translateJson(JsonObject object, String locale) {
        if (object.get("translate") != null) {
            ChatConstant chatConstant = ChatConstant.fromPath(object.get("translate").getAsString());
            if (chatConstant != null) {
                object.addProperty("text", new LocalizedChatMessage(ChatConstant.fromPath(object.get("translate").getAsString())).getMessage(locale));
                object.remove("translate");
            }
        }
        if (object.get("extra") != null) {
            for (JsonElement extra : object.getAsJsonArray("extra")) {
                translateJson(extra.getAsJsonObject(), locale);
            }
        }
        return object;
    }

    private void dropInventory(Player player) {
        dropInventory(player, false);
    }

    private void dropInventory(Player player, boolean force) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(player);
        if (GameHandler.getGameHandler().getMatch().isRunning() && (force || (team.isPresent() && !team.get().isObserver() && !isDeadUUID(player.getUniqueId())))) {
            for (ItemStack stack : player.getInventory().getContents()) {
                if (stack != null && stack.getType() != Material.AIR) {
                    player.getWorld().dropItemNaturally(player.getLocation().add(0, 0.5, 0), stack);
                }
            }
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getInventory().setExtraContents(null);
        }
    }

    private void setBlood(Player player, Boolean active) {
        sendWorldBorderPacket(player, active ? Integer.MAX_VALUE : GameHandler.getGameHandler().getMatchWorld().getWorldBorder().getWarningDistance());
    }

    private void sendTitle(Player player) {
        TranslatableComponent title = new TranslatableComponent("deathScreen.title");
        title.setColor(net.md_5.bungee.api.ChatColor.RED);
        player.showTitle(title, new TextComponent(""), 0, Integer.MAX_VALUE, 0);
    }

    private void sendWorldBorderPacket(Player player, int warningBlocks) {
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        WorldBorder playerWorldBorder = nmsPlayer.world.getWorldBorder();
        PacketPlayOutWorldBorder worldBorder = new PacketPlayOutWorldBorder(playerWorldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS);

        try {
            Field field = worldBorder.getClass().getDeclaredField("i");
            field.setAccessible(true);
            field.setInt(worldBorder, warningBlocks);
            field.setAccessible(!field.isAccessible());
        } catch (Exception e) {
            e.printStackTrace();
        }

        PacketUtils.sendPacket(player, worldBorder);
    }

    private void playDeathAnimation(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

                List<Packet> packets = new ArrayList<>();
                for (EnumItemSlot slot : EnumItemSlot.values()) {
                    packets.add(new PacketPlayOutEntityEquipment(nmsPlayer.getId(), slot,
                            net.minecraft.server.ItemStack.a));  // Removes armor, otherwise, a client-side glitch makes items
                }
                packets.add(PacketUtils.createMetadataPacket(nmsPlayer.getId(), Watchers.getHealth(0)));
                packets.add(new PacketPlayOutEntityStatus(nmsPlayer, (byte) 3));

                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (!online.equals(player)){
                        for (Packet packet : packets) {
                            PacketUtils.sendPacket(online, packet);
                        }
                    }
                }
            }
        }, 1L);
    }

    public void sendArmorStandPacket(Player player) {
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        Location loc = player.getLocation();
        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(
                Integer.MAX_VALUE, UUID.randomUUID(),     // Entity id and Entity UUID
                30,                                       // Entity type id (ArmorStand)
                loc.getX(), loc.getY() - 1.1D, loc.getZ(),// X, Y and Z Position
                0, 0, 0,                                  // X, Y and Z Motion
                (byte)2, (byte)0, (byte)2,                // Yaw, Pitch and Head Pitch
                Watchers.toList(Watchers.INVISIBLE)       // Metadata
        );
        PacketUtils.sendPacket(player, spawnPacket);
        // Create a packet to send 0 max health attribute, so that health doesn't display
        PacketUtils.sendPacket(player, PacketUtils.createHealthAttribute(Integer.MAX_VALUE));
        PacketUtils.sendPacket(player, new PacketPlayOutMount(Integer.MAX_VALUE, nmsPlayer.getId()));
        player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
    }

    public void destroyArmorStandPacket(Player player) {
        PacketUtils.sendPacket(player, new PacketPlayOutEntityDestroy(Integer.MAX_VALUE));
    }

    private void respawnPlayer(final Player player, boolean teleport) {
        UUID uuid = player.getUniqueId();
        if (!teleport) {
            this.deadPlayers.remove(uuid);
            this.hasLeftClicked.remove(uuid);
            Players.resetPlayer(player);
            setBlood(player, false);
            destroyArmorStandPacket(player);
            CardinalSpawnEvent respawnEvent = new CardinalSpawnEvent(player, Teams.getTeamByPlayer(player).get());
            GameHandler.getGameHandler().getPlugin().getServer().getPluginManager().callEvent(respawnEvent);
            PlayerNameUpdateEvent nameUpdateEvent = new PlayerNameUpdateEvent(player);
            Bukkit.getServer().getPluginManager().callEvent(nameUpdateEvent);
            return;
        }

        if (playerCanRespawn(player)) {
            this.deadPlayers.remove(uuid);
            this.hasLeftClicked.remove(uuid);
        } else {
            if (!deadPlayers.containsKey(uuid)) {
                deadPlayers.put(uuid, 0L);
                hasLeftClicked.add(uuid);

                Players.setObserver(player);

                player.showTitle(new TextComponent(""), new TextComponent(""), 0, Integer.MAX_VALUE, 0);
                GameHandler.getGameHandler().getMatch().getModules().getModule(Visibility.class).showOrHide(player);
            }
            return;
        }

        setBlood(player, false);
        destroyArmorStandPacket(player);

        player.setTitleTimes(0, 0, 10);

        CardinalSpawnEvent respawnEvent = new CardinalSpawnEvent(player, Teams.getTeamByPlayer(player).get());
        GameHandler.getGameHandler().getPlugin().getServer().getPluginManager().callEvent(respawnEvent);

        PlayerNameUpdateEvent nameUpdateEvent = new PlayerNameUpdateEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(nameUpdateEvent);

        if (this.bed && player.getBedSpawnLocation() != null) {
            player.teleport(player.getBedSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        } else {
            player.teleport(respawnEvent.getSpawn().getLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        }
    }

    private void killPlayer(final Player player, Player killer, EntityDamageEvent.DamageCause cause) {
        if (deadPlayers.containsKey(player.getUniqueId())) return;
        this.deadPlayers.put(player.getUniqueId(), System.currentTimeMillis());

        CardinalDeathEvent cardinalDeathEvent = new CardinalDeathEvent(player, killer, cause);
        Bukkit.getServer().getPluginManager().callEvent(cardinalDeathEvent);

        dropInventory(player, true);

        PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, new ArrayList<ItemStack>(), player.getExpToLevel(), 0, 0, 0, null);
        deathEvent.setDeathMessage(null);
        Bukkit.getServer().getPluginManager().callEvent(deathEvent);

        if (!isDeadUUID(player.getUniqueId())) return;

        Players.setObserver(player);

        PlayerNameUpdateEvent nameUpdateEvent = new PlayerNameUpdateEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(nameUpdateEvent);

        sendTitle(player);

        player.setGameMode(GameMode.CREATIVE);

        setBlood(player, true);
        playDeathAnimation(player);

        if (!this.spectate) sendArmorStandPacket(player);

        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0, true, false), false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, this.blackout ? Integer.MAX_VALUE : 20, 0, true, false), false);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            public void run() {
                GameHandler.getGameHandler().getMatch().getModules().getModule(Visibility.class).showOrHide(player);
            }
        }, 15L);
        if (this.auto) hasLeftClicked.add(player.getUniqueId());
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            Optional<TeamModule> team = Teams.getTeamByPlayer(player);
            if (!team.isPresent() || !team.get().isObserver()) {
                respawnPlayer(player, true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (TitleRespawn.this.isDeadUUID(player.getUniqueId())) {
                            player.showTitle(new TextComponent(""), new TextComponent(""), 0, Integer.MAX_VALUE, 0);
                            getSubtitleLogic();
                        }
                    }
                }, 1L);
            }
        }
    }

    @EventHandler
    public void onLocaleChange(PlayerLocaleChangeEvent event) {
        if (isDeadUUID(event.getPlayer().getUniqueId()) && deadPlayers.get(event.getPlayer().getUniqueId()) != 0) sendTitle(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        double finalHealth = player.getHealth() - event.getFinalDamage();
        if (finalHealth > 0.01) return;
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        event.setDamage(1);
        player.setLastDamageCause(event);
        killPlayer(player, null, event.getCause());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        double finalHealth = player.getHealth() - event.getFinalDamage();
        if (finalHealth > 0.01) return;
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        event.setDamage(1);
        player.setLastDamageCause(event);
        if (event.getActor() instanceof Player) {
            killPlayer(player, (Player)event.getActor(), event.getCause());
        } else if (event.getActor() instanceof Projectile && ((Projectile) event.getActor()).getShooter() instanceof Player) {
            killPlayer(player, (Player)((Projectile) event.getActor()).getShooter(), event.getCause());
        } else {
            killPlayer(player, null, event.getCause());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        boolean action = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK;
        if (isDeadUUID(id)) {
            event.setCancelled(true);
            if (action && !hasLeftClicked.contains(id)) hasLeftClicked.add(id);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        respawnPlayer(event.getPlayer(), true);
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            respawnPlayer(player, false);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (!team.isPresent() || !team.get().isObserver()) dropInventory(event.getPlayer());
        this.deadPlayers.remove(event.getPlayer().getUniqueId());
        this.hasLeftClicked.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerSwitchTeam(PlayerChangeTeamEvent event) {
        respawnPlayer(event.getPlayer(), GameHandler.getGameHandler().getMatch().isRunning() && !(event.getNewTeam().get().isObserver() && isDeadUUID(event.getPlayer().getUniqueId())));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSwitchTeam2(PlayerChangeTeamEvent event) {
        if (!GameHandler.getGameHandler().getMatch().isRunning() || (event.getOldTeam().isPresent() && event.getOldTeam().get().isObserver())) return;
        dropInventory(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (!this.spectate && (isDeadUUID(player.getUniqueId()) && deadPlayers.get(player.getUniqueId()) != 0)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        boolean material = event.getBucket().equals(Material.WATER_BUCKET) || event.getBucket().equals(Material.LAVA_BUCKET) || event.getBucket().equals(Material.LAVA) || event.getBucket().equals(Material.WATER) || event.getBucket().equals(Material.AIR);
        if (material && isDeadUUID(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            respawnPlayer(player, true);
        }
    }

}