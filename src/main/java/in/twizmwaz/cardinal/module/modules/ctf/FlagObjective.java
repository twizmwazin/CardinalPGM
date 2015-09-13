package in.twizmwaz.cardinal.module.modules.ctf;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.event.flag.FlagDropEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.event.flag.FlagRespawnEvent;
import in.twizmwaz.cardinal.event.flag.FlagCaptureEvent;
import in.twizmwaz.cardinal.event.flag.FlagPickupEvent;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.scoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Flags;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public class FlagObjective implements TaskedModule, GameObjective {

    private String id;
    private boolean required;           // Default: true
    private String name;
    private DyeColor color;
    private boolean show;               // Default: true
    private Post post;                  // required
    private List<Net> nets;
    private TeamModule owner;
    private boolean shared;             // Default: false
    private String carryMessage;
    private int points;                 // Default: 0
    private int pointsRate;             // Default: 0
    private FilterModule pickupFilter;
    private FilterModule captureFilter;
    private Kit pickupKit;
    private Kit dropKit;
    private Kit carryKit;
    private boolean dropOnWater;

    private Banner banner;
    private BlockFace originalFace;
    private Player picker;
    private Block currentFlagBlock;
    private ItemStack pickerHelmet;
    private boolean respawning;
    private boolean onGround;
    private int seconds = 1;
    private int respawnTime;
    private int recoverTime;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    public FlagObjective(String id,
                         boolean required,
                         String name,
                         DyeColor color,
                         boolean show,
                         Post post,
                         List<Net> nets,
                         TeamModule owner,
                         boolean shared,
                         String carryMessage,
                         int points,
                         int pointsRate,
                         FilterModule pickupFilter,
                         FilterModule captureFilter,
                         Kit pickupKit,
                         Kit dropKit,
                         Kit carryKit,
                         boolean dropOnWater) {
        this.id = id;
        this.required = required;
        this.name = name;
        this.color = color;
        this.show = show;
        this.post = post;
        this.nets = nets;
        this.owner = owner;
        this.shared = shared;
        this.carryMessage = carryMessage;
        this.points = points;
        this.pointsRate = pointsRate;
        this.pickupFilter = pickupFilter;
        this.captureFilter = captureFilter;
        this.pickupKit = pickupKit;
        this.dropKit = dropKit;
        this.carryKit = carryKit;
        this.dropOnWater = dropOnWater;

        for (RegionModule region : post.getRegions()) {
            if (region.getCenterBlock().getBlock().getState() instanceof Banner) {
                this.banner = (Banner) region.getCenterBlock().getBlock().getState();
            }
        }

        currentFlagBlock = getPost().getInitialBlock();
        originalFace = Flags.yawToFace(getPost().getYaw());
        respawning = false;
        onGround = false;
        scoreboardHandler = new GameObjectiveScoreboardHandler(this);

        respawnTime = post.getRespawnTime();
        recoverTime = post.getRecoverTime();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public TeamModule getTeam() {
        return owner;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isTouched() {
        return false;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean showOnScoreboard() {
        return show;
    }

    @Override
    public GameObjectiveScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @Override
    public GameObjectiveProximityHandler getProximityHandler() {
        return null;
    }

    public String getDisplayName() {
        return MiscUtil.convertDyeColorToChatColor(color) + getName();
    }

    public Post getPost() {
        return post;
    }

    public List<Net> getNets() {
        return nets;
    }

    public Player getPicker() {
        return picker;
    }

    public void setPicker(Player picker) {
        this.picker = picker;
    }

    public boolean isCarried() {
        return getPicker() != null;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public boolean isRespawning() {
        return respawning;
    }

    public boolean isShared() {
        return shared;
    }

    public Block getCurrentFlagBlock() {
        return currentFlagBlock;
    }

    public FilterModule getPickupFilter() {
        return pickupFilter;
    }

    public FilterModule getCaptureFilter() {
        return captureFilter;
    }

    public void respawnFlag() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            RegionModule spawn = getPost().getNextFlagSpawn();
            Block block;
            if (spawn instanceof PointRegion) {
                block = ((PointRegion) spawn).getBlock();
            } else {
                block = spawn.getRandomPoint().getBlock();
            }
            spawnFlag(block, true);
            FlagRespawnEvent e = new FlagRespawnEvent(this, getPost(), block);
            Bukkit.getServer().getPluginManager().callEvent(e);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(new LocalizedChatMessage(ChatConstant.UI_FLAG_RESPAWNED, getDisplayName() + ChatColor.RESET).getMessage(p.getLocale()));
            }
            respawning = false;
        }
    }

    private void spawnFlag(Block flagBlock, boolean post) {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (flagBlock.getRelative(BlockFace.DOWN).getType().equals(Material.WATER)) {
                if (dropOnWater) {
                    flagBlock.getRelative(BlockFace.DOWN).setType(Material.PACKED_ICE);
                } else {
                    // TODO
                }
            }
            currentFlagBlock = flagBlock;
            flagBlock.setType(banner.getMaterial());
            Banner newBanner = (Banner) flagBlock.getState();
            newBanner.setPatterns(banner.getPatterns());
            newBanner.setBaseColor(banner.getBaseColor());
            Flags.setBannerFacing(originalFace, newBanner);
            newBanner.update();
            if (!post) {
                FlagDropEvent event = new FlagDropEvent(getPicker(), this);
                Bukkit.getPluginManager().callEvent(event);
                onGround = true;
            }
            picker = null;
            pickerHelmet = null;
        }
    }

    @EventHandler
    public void onPlayerPickupFlag(FlagPickupEvent event) {
        if (event.getFlag().equals(this)) {
            if (pickupKit != null) pickupKit.apply(event.getPlayer(), null);
            pickerHelmet = event.getPlayer().getInventory().getHelmet();
            ItemStack bannerItem = new ItemStack(Material.BANNER);
            BannerMeta meta = (BannerMeta) bannerItem.getItemMeta();
            meta.setBaseColor(banner.getBaseColor());
            meta.setPatterns(banner.getPatterns());
            bannerItem.setItemMeta(meta);
            event.getPlayer().getInventory().setHelmet(bannerItem);
            event.getPlayer().getWorld().getBlockAt(currentFlagBlock.getLocation()).setType(Material.AIR);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(new LocalizedChatMessage(ChatConstant.UI_FLAG_PICKED_UP, getDisplayName() + ChatColor.RESET, Teams.getTeamByPlayer(event.getPlayer()).get().getColor() + event.getPlayer().getName()).getMessage(p.getLocale()));
            }
            setPicker(event.getPlayer());
            onGround = false;
        }
    }

    @EventHandler
    public void onDropFlag(FlagDropEvent event) {
        if (event.getFlag().equals(this)) {
            if (dropKit != null) dropKit.apply(event.getPlayer(), null);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(new LocalizedChatMessage(ChatConstant.UI_FLAG_DROPPED, getDisplayName() + ChatColor.RESET).getMessage(p.getLocale()));
            }
        }
    }

    @EventHandler
    public void onCaptureFlag(FlagCaptureEvent event) {
        Player p = event.getPlayer();
        if (event.getFlag().equals(this)) {
            setPicker(null);
            p.getInventory().setHelmet(pickerHelmet);
            pickerHelmet = null;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(new LocalizedChatMessage(ChatConstant.UI_FLAG_CAPTURED, getDisplayName() + ChatColor.RESET, Teams.getTeamByPlayer(p).get().getColor() + p.getName()).getMessage(player.getLocale()));
            }
            if (points > 0 || event.getNet().getPoints() > 0) {
                for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                    TeamModule scored = owner != null ? owner : Teams.getTeamByPlayer(p).get();
                    if (scored != null && scored == score.getTeam()) {
                        int pointsToAdd = points > 0 ? points : event.getNet().getPoints();
                        score.setScore(score.getScore() + pointsToAdd);
                        Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(score));
                    }
                }
            }
            respawning = true;
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (onGround && GameHandler.getGameHandler().getMatch().isRunning()) {
            if (Teams.getTeamByPlayer(event.getPlayer()).isPresent() && !Teams.getTeamByPlayer(event.getPlayer()).get().isObserver()) {
                TeamModule team = Teams.getTeamByPlayer(event.getPlayer()).get();
                if (!team.equals(owner) && event.getPlayer().getHealth() > 0) {
                    if (event.getTo().getBlock().equals(currentFlagBlock) && !event.getFrom().getBlock().equals(currentFlagBlock)) {
                        FlagPickupEvent e = new FlagPickupEvent(event.getPlayer(), this);
                        Bukkit.getServer().getPluginManager().callEvent(e);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material type = event.getBlock().getType();
        if (event.getBlock().equals(currentFlagBlock)) {
            if (type.equals(Material.BANNER) || type.equals(Material.STANDING_BANNER) || type.equals(Material.WALL_BANNER)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_BREAK_FLAG).getMessage(event.getPlayer().getLocale()));
            }
        } else if (event.getBlock().equals(currentFlagBlock.getRelative(BlockFace.DOWN))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_BREAK_BLOCK_UNDER_FLAG).getMessage(event.getPlayer().getLocale()));
        }
    }

    @EventHandler
    public void onDead(PlayerDeathEvent event) {
        if (getPicker() != null && event.getEntity().equals(getPicker())) {
            spawnFlag(event.getEntity().getLocation().getBlock(), false);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();
            if (getPicker() != null && getPicker().equals(p)) {
                if (event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.BANNER)) {
                    event.setCancelled(true);
                    p.closeInventory();
                    p.getInventory().setHelmet(pickerHelmet);
                    spawnFlag(p.getLocation().getBlock(), false);
                }
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().getData().equals(banner)) {
            event.getEntity().remove();
        }
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (seconds <= MatchTimer.getTimeInSeconds()) {
                seconds++;
                if (respawning) {
                    if (respawnTime == 0) {
                        respawnFlag();
                        respawning = false;
                        respawnTime = getPost().getRespawnTime();
                    }
                    respawnTime--;
                }

                if (onGround) {
                    if (recoverTime == 0) {
                        respawning = true;
                        onGround = false;
                        recoverTime = getPost().getRecoverTime();
                        currentFlagBlock.setType(Material.AIR);
                    }
                    recoverTime--;
                }

                if (isCarried()) {
                    if (pointsRate > 0 || getPost().getPointsRate() > 0) {
                        int points = pointsRate > 0 ? pointsRate : getPost().getPointsRate();
                        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                            TeamModule scored = owner != null ? owner : Teams.getTeamByPlayer(getPicker()).get();
                            if (scored != null && scored == score.getTeam()) {
                                score.setScore(score.getScore() + points);
                                Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(score));
                            }
                        }
                    }
                }
            }
        }
    }
}
