package in.twizmwaz.cardinal.module.modules.blockdrops;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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

    protected Blockdrops(final RegionModule region, final FilterModule filter, final Set<ItemStack> drops, final Material replace, final int experience, final boolean wrongTool) {
        this.region = region;
        this.filter = filter;
        this.drops = drops;
        this.replace = replace;
        this.experience = experience;
        this.wrongTool = wrongTool;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            if (filter == null || filter.evaluate(player, block).equals(FilterState.ALLOW)) {
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
                    block.setType(replace);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!event.isCancelled()) {
            Player player = TntTracker.getWhoPlaced(event.getEntity()) != null && Bukkit.getOfflinePlayer(TntTracker.getWhoPlaced(event.getEntity())).isOnline() ? Bukkit.getPlayer(TntTracker.getWhoPlaced(event.getEntity())) : null;
            if (player != null) {
                for (Block block : event.blockList()) {
                    if (filter == null || filter.evaluate(player, block).equals(FilterState.ALLOW)) {
                        if (region == null || region.contains(new BlockRegion(null, block.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
                            for (ItemStack drop : this.drops) {
                                GameHandler.getGameHandler().getMatchWorld().dropItemNaturally(block.getLocation(), drop);
                            }
                            if (this.experience != 0) {
                                ExperienceOrb xp = GameHandler.getGameHandler().getMatchWorld().spawn(block.getLocation(), ExperienceOrb.class);
                                xp.setExperience(this.experience);
                            }
                            block.setType(replace);
                        }
                    }
                }
            }
        }
    }
}
