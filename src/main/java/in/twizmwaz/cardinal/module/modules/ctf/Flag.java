package in.twizmwaz.cardinal.module.modules.ctf;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.ctf.event.FlagRespawnEvent;
import in.twizmwaz.cardinal.module.modules.ctf.event.PlayerCaptureFlagEvent;
import in.twizmwaz.cardinal.module.modules.ctf.net.Net;
import in.twizmwaz.cardinal.module.modules.ctf.post.Post;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

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

    public void respawnFlag() {
        RegionModule spawn = getPost().getNextFlagSpawn();
        Block block;
        if (spawn instanceof PointRegion) {
            block = ((PointRegion) spawn).getBlock();
        } else {
            block = spawn.getRandomPoint().getBlock();
        }
        block.getState().setMaterialData(banner.getMaterialData());
        block.getState().update();

        FlagRespawnEvent e = new FlagRespawnEvent(this, getPost());
        Bukkit.getServer().getPluginManager().callEvent(e);
    }

    @EventHandler
    public void onCaptureFlag(PlayerCaptureFlagEvent event) {
        if (event.getFlag().equals(this)) {
            setPicker(null);
            Bukkit.broadcastMessage(event.getPlayer().getName() + " captured " + getName());
        }
    }

    public void run() {

    }
}
