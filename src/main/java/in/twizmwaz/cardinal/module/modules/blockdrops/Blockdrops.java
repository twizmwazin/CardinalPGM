package in.twizmwaz.cardinal.module.modules.blockdrops;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.Set;

public class Blockdrops implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    private final RegionModule region;
    private final FilterModule filter;
    private final Set<ItemStack> drops;
    private final Material replace;
    private final int experience;
    private final boolean wrongTool;
    private final double fallchance;
    private final double landchance;
    private final double fallspeed;

    protected Blockdrops(final RegionModule region, final FilterModule filter, final Set<ItemStack> drops, final Material replace, final int experience, final boolean wrongTool, final double fallchance, final double landchance, final double fallspeed) {
        this.region = region;
        this.filter = filter;
        this.drops = drops;
        this.replace = replace;
        this.experience = experience;
        this.wrongTool = wrongTool;
        this.fallchance = fallchance;
        this.landchance = landchance;
        this.fallspeed = fallspeed;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (filter != null) {
                Player player = event.getPlayer();
                Block block = event.getBlock();
                if (!filter.evaluate(player).equals(FilterState.DENY) && !filter.evaluate(block).equals(FilterState.DENY)) {
                    if (region == null || region.contains(new BlockRegion(null, block.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
                        if (!this.wrongTool) {
                            if (block.getDrops() != null && block.getDrops().size() > 0) {
                                for (ItemStack drop : this.drops) {
                                    GameHandler.getGameHandler().getMatchWorld().dropItemNaturally(block.getLocation(), drop);
                                }
                                if (this.experience != 0) {
                                    ExperienceOrb xp = GameHandler.getGameHandler().getMatchWorld().spawn(block.getLocation(), ExperienceOrb.class);
                                    xp.setExperience(this.experience);
                                }
                            }
                        } else {
                            for (ItemStack drop : this.drops) {
                                GameHandler.getGameHandler().getMatchWorld().dropItemNaturally(block.getLocation(), drop);
                            }
                            ExperienceOrb xp = GameHandler.getGameHandler().getMatchWorld().spawn(block.getLocation(), ExperienceOrb.class);
                            xp.setExperience(this.experience);
                        }
                        event.setCancelled(true);
                        block.setType(replace);
                    }
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (fallchance != 0.0) {
            if (region.contains(new BlockRegion(null, event.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
                double fallchance = event.blockList().size() * this.fallchance;
                for (double i = fallchance; i > 0; i--) {
                    Block block = event.blockList().get((int) i - 1);
                    Vector vector = Vector.getRandom();
                    FallingBlock fallingBlock = GameHandler.getGameHandler().getMatchWorld().spawnFallingBlock(event.getLocation(), block.getType(), block.getData());
                    fallingBlock.setVelocity(vector);
                    fallingBlock.setDropItem(false);
                }

            }
        }
    }

    @EventHandler
    public void onEntiyChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            if (new Random().nextGaussian() > landchance)
                event.setCancelled(true);
        }
    }
}

