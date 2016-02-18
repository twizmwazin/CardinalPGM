package in.twizmwaz.cardinal.module.modules.titleRespawn;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.TaskedModule;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.respawn.RespawnModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Items;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TitleRespawn implements TaskedModule {

    private final int delay;
    private final boolean auto;
    private final boolean blackout;
    private final boolean spectate;
    private final boolean bed;
    private final String message;

    DecimalFormat formatter = new DecimalFormat("0.0");

    private final Map<UUID, Boolean> hasLeftClicked = new HashMap<>();
    private final Map<UUID, Long> deadPlayers = new HashMap<>();

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

    public boolean isAllowedToRespawn(UUID id) {
        double timeLeftToRespawn = this.delay - ((System.currentTimeMillis() - deadPlayers.get(id)) / 1000.0);
        return this.isDeadUUID(id) && timeLeftToRespawn <= 0;
    }

    public boolean isDeadUUID(UUID id) {
        return this.deadPlayers.containsKey(id);
    }

    private boolean hasLeftClicked(UUID id) {
        return hasLeftClicked.get(id);
    }

    public void getSubtitleLogic() {
        for (UUID id : this.deadPlayers.keySet()) {
            double timeLeftToRespawn = this.delay - ((System.currentTimeMillis() - deadPlayers.get(id)) / 1000.0);
            Player player = Bukkit.getPlayer(id);
            if (timeLeftToRespawn > 0) {
                if (hasLeftClicked(id) || this.auto) {
                    player.setSubtitle(TextComponent.fromLegacyText(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_DEATH_RESPAWN_CONFIRMED_TIME, ChatColor.AQUA + formatter.format(timeLeftToRespawn).replace(",", ".") + ChatColor.GREEN).getMessage(player.getLocale())));
                } else {
                    player.setSubtitle(TextComponent.fromLegacyText(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_DEATH_RESPAWN_UNCONFIRMED_TIME, ChatColor.AQUA + formatter.format(timeLeftToRespawn).replace(",",".") + ChatColor.GREEN).getMessage(player.getLocale())));
                }
            } else if (isAllowedToRespawn(id)) {
                if (player.getInventory().getItem(2) == null || !player.getInventory().getItem(2).getType().equals(Material.LEATHER_HELMET)) {
                    ItemStack picker = Items.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                            ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(player.getLocale()) : new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(player.getLocale())),
                            Collections.singletonList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(player.getLocale())));
                    player.getInventory().setItem(2, picker);
                }
                if ((hasLeftClicked(id) || this.auto) /*&& filter allows to respawn*/) {
                    respawnPlayer(player);
                } else {
                    /*if (this.message != null && filter doesn't allow respawn) {
                        setSubtitleMessage(player);
                    } else {
                        player.setSubtitle(TextComponent.fromLegacyText(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_RESPAWN_CLICK).getMessage(player.getLocale())));
                    }*/
                    player.setSubtitle(TextComponent.fromLegacyText(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_DEATH_RESPAWN_UNCONFIRMED).getMessage(player.getLocale())));
                }
            }
        }
    }

    public void setSubtitleMessage(Player player) {
        player.setSubtitle(TextComponent.fromLegacyText(ChatColor.GREEN + this.message));
    }

    private void dropInventory(Player player) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(player);
        if (!team.get().isObserver() && !isDeadUUID(player.getUniqueId()) && GameHandler.getGameHandler().getMatch().isRunning()) {
            for (ItemStack stack : player.getInventory().getArmorContents()) {
                if (stack != null && stack.getType() != Material.AIR) {
                    player.getWorld().dropItemNaturally(player.getLocation().add(0, 0.5, 0), stack);
                }
            }
            for (int i = 0; i < 36; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack != null && stack.getType() != Material.AIR) {
                    player.getWorld().dropItemNaturally(player.getLocation().add(0, 0.5, 0), stack);
                }
            }
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }
    }

    public void setBlood(Player player, Boolean active) {
        sendWorldBorderPacket(player, active ? Integer.MAX_VALUE : GameHandler.getGameHandler().getMatchWorld().getWorldBorder().getWarningDistance());
    }

    protected void sendWorldBorderPacket(Player player, int warningBlocks) {
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

        nmsPlayer.playerConnection.sendPacket(worldBorder);
    }

    public void playDeathAnimation(Player player) {
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        DataWatcher data = new DataWatcher(nmsPlayer);
        data.a(6, 0.0F);

        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(nmsPlayer.getId(), nmsPlayer.getDataWatcher(), false);
        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(nmsPlayer, (byte) 3);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)){
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(metadata);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
            }
        }

        nmsPlayer.setSize(0.2F, 0.2F);
        nmsPlayer.motY = 0.10000000149011612D;
    }

    public void sendArmorStandPacket(Player player) {
        try {
            EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

            EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) GameHandler.getGameHandler().getMatchWorld()).getHandle());
            entityArmorStand.setHealth(1.0F);
            entityArmorStand.setInvisible(true);

            PacketPlayOutSpawnEntityLiving armorStand = new PacketPlayOutSpawnEntityLiving(entityArmorStand);

            armorStand.a = Integer.MAX_VALUE; //Entity ID
            armorStand.c = (int) (player.getLocation().getX() * 32.0D); // X
            armorStand.d = (int) ((player.getLocation().getY() - 1.1D) * 32.0D); // Y
            armorStand.e = (int) (player.getLocation().getZ() * 32.0D);// Z

            PacketPlayOutAttachEntity attachEntity = new PacketPlayOutAttachEntity();
            attachEntity.b = nmsPlayer.getId();
            attachEntity.c = Integer.MAX_VALUE;

            nmsPlayer.playerConnection.sendPacket(armorStand);
            nmsPlayer.playerConnection.sendPacket(attachEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyArmorStandPacket(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(Integer.MAX_VALUE));
    }

    public void respawnPlayer(final Player player) {
        boolean bedLocation = false;
        Location location = player.getWorld().getSpawnLocation();
        if (this.bed && player.getBedSpawnLocation() != null) {
            bedLocation = true;
            location = player.getBedSpawnLocation();
        }

        destroyArmorStandPacket(player);
        Players.resetPlayer(player);

        PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(player, location, bedLocation);
        GameHandler.getGameHandler().getPlugin().getServer().getPluginManager().callEvent(respawnEvent);

        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.PLAYING) && !Teams.getTeamByPlayer(player).get().isObserver()) {
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            player.setGameMode(GameMode.CREATIVE);
        }

        player.setTitleTimes(0, 0, 10);
        player.resetTitle();

        setBlood(player, false);

        this.deadPlayers.remove(player.getUniqueId());
        this.hasLeftClicked.put(player.getUniqueId(), false);

        PlayerNameUpdateEvent nameUpdateEvent = new PlayerNameUpdateEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(nameUpdateEvent);

        player.teleport(respawnEvent.getRespawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);

        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Cardinal.getInstance(), new Runnable() {
            public void run() {
                Optional<TeamModule> team = Teams.getTeamByPlayer(player);
                if (team.get().isObserver() || !GameHandler.getGameHandler().getMatch().isRunning()) {
                    GameHandler.getGameHandler().getMatch().getModules().getModule(RespawnModule.class).giveObserversKit(player);
                }
            }
        }, 1L);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (GameHandler.getGameHandler().getMatch().isRunning() && !isDeadUUID(player.getUniqueId())) {
                online.showPlayer(player);
            }
        }
    }

    public void killPlayer(final Player player) {
        PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, new ArrayList<ItemStack>(), player.getExpToLevel(), 0, 0, 0, ChatColor.GRAY + "" + ChatColor.BOLD + "%s died from unknown causes");
        Bukkit.getServer().getPluginManager().callEvent(deathEvent);

        if (deathEvent.getDeathMessage() != null) {
            Bukkit.getServer().broadcastMessage(String.format(deathEvent.getDeathMessage(), player.getDisplayName() + ChatColor.RESET));
        }

        player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 2, 1);
        player.setAffectsSpawning(false);
        player.setCollidesWithEntities(false);
        player.setCanPickupItems(false);
        player.setPotionParticles(false);

        dropInventory(player);

        this.deadPlayers.put(player.getUniqueId(), System.currentTimeMillis());
        PlayerNameUpdateEvent nameUpdateEvent = new PlayerNameUpdateEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(nameUpdateEvent);

        String title = new LocalizedChatMessage(ChatConstant.UI_DEATH_RESPAWN_DIED).getMessage(player.getLocale());
        player.showTitle(new TextComponent(ChatColor.RED + title), new TextComponent(""), 0, Integer.MAX_VALUE, 0);

        player.setGameMode(GameMode.CREATIVE);
        Players.resetPlayer(player);
        setBlood(player, true);
        playDeathAnimation(player);

        if (!this.spectate) {
            sendArmorStandPacket(player);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 0));
        if (this.blackout) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0));
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            public void run() {
                if (!deadPlayers.containsKey(player.getUniqueId())) return;
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    online.hidePlayer(player);
                }
            }
        }, 20L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.PLAYING) && !Teams.getTeamByPlayer(player).get().isObserver()) {
                double finalHealth = player.getHealth() - event.getDamage();
                if (finalHealth > 0.0) {
                    return;
                }
                player.getWorld().playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 1, 1.2F);
                player.setLastDamageCause(event);
                killPlayer(player);

            } else {
                respawnPlayer(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID id = event.getPlayer().getUniqueId();
        boolean action = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK;
        if (isDeadUUID(id)) {
            event.setCancelled(true);
            if (!this.auto && action) {
                if (isAllowedToRespawn(id)) {
                    respawnPlayer(player);
                } else {
                    this.hasLeftClicked.put(id, true);
                }
            }
        }
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.hasLeftClicked.put(player.getUniqueId(), false);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        respawnPlayer(event.getPlayer());
        this.hasLeftClicked.put(event.getPlayer().getUniqueId(), false);
        for (UUID id : this.deadPlayers.keySet()) {
            event.getPlayer().hidePlayer(Bukkit.getPlayer(id));
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (isDeadUUID(player.getUniqueId())) {
                this.deadPlayers.remove(player.getUniqueId());
                PlayerNameUpdateEvent nameUpdateEvent = new PlayerNameUpdateEvent(player);
                Bukkit.getServer().getPluginManager().callEvent(nameUpdateEvent);
                Players.resetPlayer(player);
                setBlood(player, false);
                destroyArmorStandPacket(player);
            }
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Cardinal.getInstance(), new Runnable() {
                public void run() {
                    GameHandler.getGameHandler().getMatch().getModules().getModule(RespawnModule.class).giveObserversKit(player);
                }
            }, 1L);
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
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            respawnPlayer(event.getPlayer());
        }
        this.deadPlayers.remove(event.getPlayer().getUniqueId());
        this.hasLeftClicked.put(event.getPlayer().getUniqueId(), false);
    }

    @EventHandler
    public void onPlayerSwitchTeam2(PlayerChangeTeamEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            dropInventory(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking() && isDeadUUID(player.getUniqueId()) && !this.spectate) {
            sendArmorStandPacket(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!this.spectate && isDeadUUID(player.getUniqueId())) {
            sendArmorStandPacket(player);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        boolean material = event.getBucket().equals(Material.WATER_BUCKET) || event.getBucket().equals(Material.LAVA_BUCKET) || event.getBucket().equals(Material.LAVA) || event.getBucket().equals(Material.WATER) || event.getBucket().equals(Material.AIR);
        if (material && isDeadUUID(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

}