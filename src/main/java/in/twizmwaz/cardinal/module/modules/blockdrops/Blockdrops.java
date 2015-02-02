package in.twizmwaz.cardinal.module.modules.blockdrops;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Set;

public class Blockdrops implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    private final RegionModule region;
    private final ModuleCollection<FilterModule> filter;
    private final Set<ItemStack> drops;
    private final Material replace;
    private final int experience;
    private final boolean wrongtool;

    protected Blockdrops(final RegionModule region, final ModuleCollection<FilterModule> filter, final Set<ItemStack> drops, final Material replace, final int experience, final boolean wrongtool) {
        this.region = region;
        this.filter = filter;
        this.drops = drops;
        this.replace = replace;
        this.experience = experience;
        this.wrongtool = wrongtool;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block blockBroken = event.getBlock();
        boolean proceed = true;
        for (FilterModule filter : this.filter) {
            if ((!filter.evaluate(player).equals(FilterState.DENY) && !filter.evaluate(blockBroken).equals(FilterState.DENY))) {
                proceed = false;
            }
        }
        if (proceed) {
            if (region != null) {
                if (region.contains(new BlockRegion(null, blockBroken.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
                    if (!wrongtool) {
                        if (blockBroken.getDrops() != null) {
                            Blockdrop(blockBroken);
                        }
                    } else {
                        Blockdrop(blockBroken);
                    }
                    event.setCancelled(true);
                }

            }
        }
    }

    private void Blockdrop(Block block){
        for (ItemStack item : drops) {
            if (item != null)
                block.getWorld().dropItemNaturally(block.getLocation(), item);
        }
        block.setType(replace);

        ExperienceOrb xp = GameHandler.getGameHandler().getMatchWorld().spawn(block.getLocation(), ExperienceOrb.class);
        xp.setExperience(experience);
    }

}
