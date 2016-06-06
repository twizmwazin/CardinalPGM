package in.twizmwaz.cardinal.module.modules.renewables;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Teams;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.PacketPlayOutWorldEvent;
import net.minecraft.server.SoundCategory;
import net.minecraft.server.SoundEffectType;
import net.minecraft.server.World;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Renewable implements TaskedModule {

    private RegionModule region;
    private FilterModule renewFilter;
    private FilterModule replaceFilter;
    private FilterModule shuffleFilter;
    private RenewMode mode;
    private double ratePerTick;
    private double intervalChance;
    private boolean grow;
    private boolean particles;
    private boolean sound;
    private int avoidPlayers;

    private Random random = new Random();
    private double renewals;

    private Map<BlockVector, MaterialData> blocks = new HashMap<>();
    private List<BlockVector> toRenew = Lists.newArrayList();
    private List<Integer> tasks = Lists.newArrayList();

    private static BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
    
    Renewable(RegionModule region,
              FilterModule renewFilter,
              FilterModule replaceFilter, 
              FilterModule shuffleFilter, 
              double rate,
              double interval, 
              boolean grow, 
              boolean particles, 
              boolean sound, 
              int avoidPlayers) {
        this.region = region;
        this.renewFilter = renewFilter;
        this.replaceFilter = replaceFilter;
        this.shuffleFilter = shuffleFilter;
        this.mode = interval < 0 ? RenewMode.RATE : RenewMode.INTERVAL;
        this.intervalChance = 0.1 / interval;
        this.ratePerTick = rate / 20;
        this.grow = grow;
        this.particles = particles;
        this.sound = sound;
        this.avoidPlayers = avoidPlayers * avoidPlayers;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning() && mode.equals(RenewMode.RATE)) {
            renewals += ratePerTick;

            int fails = 0, maxFails = 5 + (toRenew.size() / 4);

            while(fails < maxFails && toRenew.size() > 0 && renewals > 1) {
                BlockVector loc = toRenew.get(random.nextInt(toRenew.size()));

                Boolean renew = attemptRenew(loc);
                if (renew != null) {
                    if (renew) renewals--;
                    else fails++;
                }

            }
            if (renewals > 1) renewals -= (int) renewals;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        editedBlock(event.getBlock().getLocation(), event.getBlock().getState().getMaterialData());
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        editedBlock(event.getBlock().getLocation(), event.getBlockReplacedState().getMaterialData());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent event) {
        editedBlock(event.getBlockClicked().getLocation(), event.getBlockClicked().getState().getMaterialData());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Block relative = event.getBlockClicked().getRelative(event.getBlockFace());
        editedBlock(relative.getLocation(), relative.getState().getMaterialData());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBlow(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            editedBlock(block.getLocation(), block.getState().getMaterialData());
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (int id : tasks) {
            Bukkit.getScheduler().cancelTask(id);
        }
        tasks.clear();
    }

    public void stopTask(int id) {
        Bukkit.getScheduler().cancelTask(id);
        tasks.remove((Integer) id);
    }

    private void editedBlock(Vector loc, MaterialData save) {
        if (isInRegion(loc)) {
            BlockVector block = loc.toBlockVector();
            if (!blocks.containsKey(block)) {
                blocks.put(block, save);
            }
            if (!toRenew.contains(block) && renewFilter.evaluate(save).equals(FilterState.ALLOW)) {
                toRenew.add(block);
                if (mode.equals(RenewMode.INTERVAL)) {
                    RenewRunnable renewRunnable = new RenewRunnable(this, block);
                    int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cardinal.getInstance(), renewRunnable, 0L, 1L);
                    renewRunnable.setTask(taskId);
                    tasks.add(taskId);
                }
            }
        }
    }

    public Boolean attemptRenew(BlockVector loc) {
        Block block = loc.toLocation(GameHandler.getGameHandler().getMatchWorld()).getBlock();

        if (isRenewed(block)) {
            toRenew.remove(loc);
            return null;
        } else if (canRenew(block)) {
            resetBlock(block);
            toRenew.remove(loc);
            return true;
        } else {
            return false;
        }
    }

    private void resetBlock(Block block) {
        MaterialData original = blocks.get(block.getLocation().toBlockVector());

        block.setTypeIdAndData(original.getItemTypeId(), original.getData(), true);

        World nmsWorld = ((CraftWorld) GameHandler.getGameHandler().getMatchWorld()).getHandle();
        if (sound) {
            SoundEffectType sound = CraftMagicNumbers.getBlock(original.getItemType()).getSoundEffects();
            nmsWorld.playSoundEffect(null, block.getX(), block.getY(), block.getZ(), sound.breakSound(), SoundCategory.BLOCKS, sound.b(), sound.a());
        }
        if (particles) {
            Vector loc = block.getLocation().add(0.5, 0.5, 0.5);
            PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(2001, new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), original.getItemTypeId(), false);
            PacketUtils.broadcastPacket(packet);
        }
    }

    private boolean hasAdjacentBlocks(Block block) {
        for (BlockFace face : faces) {
            Block relative = block.getRelative(face);
            if (!relative.getType().equals(Material.AIR) && isRenewed(relative)) return true;
        }
        return false;
    }

    private boolean isRenewed(Block block) {
        if (!isInRegion(block.getLocation())) return false;
        BlockVector loc = block.getLocation().toBlockVector();
        if (!blocks.containsKey(loc)) {
            return true;
        } else if (shuffleFilter.evaluate(blocks.get(loc)).equals(FilterState.ALLOW)) {
            return shuffleFilter.evaluate(block).equals(FilterState.ALLOW);
        } else {
            return blocks.get(loc).equals(block.getState().getMaterialData());
        }
    }

    public boolean canRenew(Block block) {
        BlockVector pos = block.getLocation().toVector().toBlockVector();

        if(isInRegion(pos) && blocks.containsKey(pos) && !blocks.get(pos).getItemType().equals(Material.AIR)
                && replaceFilter.evaluate(block).equals(FilterState.ALLOW) && (!grow || hasAdjacentBlocks(block))) {
            if (avoidPlayers != 0) {
                for (TeamModule team : Teams.getTeams()) {
                    if (team.isObserver()) continue;
                    for (Player player : (List<Player>) team) {
                        if (player.getLocation().plus(0, 0.5, 0).distanceSquared(block.getLocation().plus(0.5, 0.5, 0.5)) < avoidPlayers) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean isInRegion(Vector pos) {
        Vector vec = new Vector(pos.getBlockX() + 0.5, pos.getBlockY() + 0.5, pos.getBlockZ() + 0.5);
        return region.contains(vec);
    }

    enum RenewMode {
        RATE(),
        INTERVAL();
    }

    private class RenewRunnable implements Runnable {

        private Renewable renewable;
        private BlockVector toRenew;
        private int taskId;

        public RenewRunnable(Renewable renewable, BlockVector block) {
            this.renewable = renewable;
            this.toRenew = block;
        }

        public void setTask(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            if (Math.random() <= renewable.intervalChance) {
                Boolean renew = renewable.attemptRenew(toRenew);
                if (renew == null || renew) {
                    renewable.stopTask(taskId);
                }
            }
        }

    }

}
