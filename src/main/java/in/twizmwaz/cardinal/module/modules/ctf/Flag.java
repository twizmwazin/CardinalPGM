package in.twizmwaz.cardinal.module.modules.ctf;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.ctf.event.*;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
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
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public class Flag implements TaskedModule {

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
    private Player picker;
    private Block currentFlagBlock;
    private ItemStack pickerHelmet;
    private boolean respawning;
    private int timer = 0;
    private int respawnTime;

    public Flag(String id,
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
        respawning = false;
    }

    public List<String> debug() {
        List<String> debug = Lists.newArrayList();
        debug.add("---------- DEBUG Flag ----------");
        debug.add("String id = " + id);
        debug.add("boolean required = " + required);
        debug.add("String name = " + name);
        debug.add("DyeColor color = " + (color == null ? "None" : color.name()));
        debug.add("boolean show = " + show);
        debug.add("Post post = " + post.getId());
        debug.add("TeamModule owner = " + (owner == null ? "None" : owner.getName()));
        debug.add("boolean shared = " + shared);
        debug.add("String carryMessage = " + carryMessage);
        debug.add("int points = " + points);
        debug.add("int pointsRate = " + pointsRate);
        debug.add("FilterModule pickupFilter = " + (pickupFilter == null ? "None" : pickupFilter.getName()));
        debug.add("FilterModule captureFilter = " + (captureFilter == null ? "None" : captureFilter.getName()));
        debug.add("Kit pickupKit = " + (pickupKit == null ? "None" : pickupKit.getName()));
        debug.add("Kit dropKit = " + (dropKit == null ? "None" : dropKit.getName()));
        debug.add("Kit carryKit = " + (carryKit == null ? "None" : carryKit.getName()));
        debug.add("boolean dropOnWater = " + dropOnWater);
        debug.add("--------------------------------");
        return debug;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public String getName() {
        return name;
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

    public void respawnFlag() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            RegionModule spawn = getPost().getNextFlagSpawn();
            Block block;
            if (spawn instanceof PointRegion) {
                block = ((PointRegion) spawn).getBlock();
            } else {
                block = spawn.getRandomPoint().getBlock();
            }
            currentFlagBlock = block;
            block.setType(banner.getMaterial());
            Banner newBanner = (Banner) block.getState();
            newBanner.setPatterns(banner.getPatterns());
            newBanner.setBaseColor(banner.getBaseColor());
            Flags.setBannerFacing(getPost().getYaw(), newBanner, true);
            newBanner.update();

            FlagRespawnEvent e = new FlagRespawnEvent(this, getPost(), block);
            Bukkit.getServer().getPluginManager().callEvent(e);
            Bukkit.broadcastMessage(getDisplayName() + ChatColor.RESET + " has respawned");
            respawning = false;
        }
    }

    @EventHandler
    public void onPlayerPickupFlag(PlayerPickupFlagEvent event) {
        if (event.getFlag().equals(this)) {
            pickerHelmet = event.getPlayer().getInventory().getHelmet();
            ItemStack bannerItem = new ItemStack(Material.BANNER);
            BannerMeta meta = (BannerMeta) bannerItem.getItemMeta();
            meta.setBaseColor(banner.getBaseColor());
            meta.setPatterns(banner.getPatterns());
            bannerItem.setItemMeta(meta);
            event.getPlayer().getInventory().setHelmet(bannerItem);
            event.getPlayer().getWorld().getBlockAt(banner.getLocation()).setType(Material.AIR);
        }
    }

    @EventHandler
    public void onCaptureFlag(PlayerCaptureFlagEvent event) {
        Player p = event.getPlayer();
        if (event.getFlag().equals(this)) {
            setPicker(null);
            p.getInventory().setHelmet(pickerHelmet);
            pickerHelmet = null;
            Bukkit.broadcastMessage(getDisplayName() + ChatColor.RESET + " captured by " + Teams.getTeamByPlayer(p).get().getColor() + p.getName());

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
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().equals(currentFlagBlock) && event.getBlock().getType().equals(Material.BANNER)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You can not break flags.");
        }
    }


    @Override
    public void run() {
        if (respawning && timer % 20 == 0) {
            respawnTime = getPost().getRespawnTime();
            if (respawnTime == 0) {
                respawnFlag();
                respawning = false;
            }
            respawnTime --;
        }

        if (isCarried() && pointsRate > 0 && timer % 20 == 0) {
            for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                TeamModule scored = owner != null ? owner : Teams.getTeamByPlayer(getPicker()).get();
                if (scored != null && scored == score.getTeam()) {
                    score.setScore(score.getScore() + pointsRate);
                    Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(score));
                }
            }
        }

        if (timer > 100) timer = 0;
        timer ++;
    }
}
