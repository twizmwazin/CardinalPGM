package in.twizmwaz.cardinal.module.modules.blockdrops;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Blockdrops implements Module {

    private final RegionModule region;
    private final FilterModule filter;
    private final Set<ItemStack> drops;
    private final Material replaceType;
    private final int replaceDamage;
    private final int experience;
    private final boolean wrongTool;

    protected Blockdrops(final RegionModule region, final FilterModule filter, final Set<ItemStack> drops, final Material replaceType, final int replaceDamage, final int experience, final boolean wrongTool) {
        this.region = region;
        this.filter = filter;
        this.drops = drops;
        this.replaceType = replaceType;
        this.replaceDamage = replaceDamage;
        this.experience = experience;
        this.wrongTool = wrongTool;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            if ((filter == null || filter.evaluate(player, block, event).equals(FilterState.ALLOW))) {
                if (region == null || region.contains(block.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5)))) {
                    if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                        if (!this.wrongTool) {
                            if (block.getDrops() != null && block.getDrops().size() > 0) {
                                for (ItemStack drop : this.drops) {
                                    GameHandler.getGameHandler().getMatchWorld().dropItemNaturally(block.getLocation().clone().add(.5, .5, .5), drop);
                                }
                                if (this.experience != 0) {
                                    ExperienceOrb xp = GameHandler.getGameHandler().getMatchWorld().spawn(block.getLocation(), ExperienceOrb.class);
                                    xp.setExperience(this.experience);
                                }
                            }
                        } else {
                            for (ItemStack drop : this.drops) {
                                GameHandler.getGameHandler().getMatchWorld().dropItemNaturally(block.getLocation().clone().add(.5, .5, .5), drop);
                            }
                            ExperienceOrb xp = GameHandler.getGameHandler().getMatchWorld().spawn(block.getLocation(), ExperienceOrb.class);
                            xp.setExperience(this.experience);
                        }
                    }
                    event.setCancelled(true);
                    block.setType(replaceType);
                    block.setData((byte) replaceDamage);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!event.isCancelled()) {
            Player player = TntTracker.getWhoPlaced(event.getEntity()) != null && Bukkit.getOfflinePlayer(TntTracker.getWhoPlaced(event.getEntity())).isOnline() ? Bukkit.getPlayer(TntTracker.getWhoPlaced(event.getEntity())) : null;
            if (player != null) {
                List<Block> toRemove = new ArrayList<>();
                for (Block block : event.blockList()) {
                    if (filter == null || filter.evaluate(player, block, event).equals(FilterState.ALLOW)) {
                        if (region == null || region.contains(block.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5)))) {
                            for (ItemStack drop : this.drops) {
                                GameHandler.getGameHandler().getMatchWorld().dropItemNaturally(block.getLocation(), drop);
                            }
                            if (this.experience != 0) {
                                ExperienceOrb xp = GameHandler.getGameHandler().getMatchWorld().spawn(block.getLocation(), ExperienceOrb.class);
                                xp.setExperience(this.experience);
                            }
                            toRemove.add(block);
                            block.setType(replaceType);
                            block.setData((byte) replaceDamage);
                        }
                    }
                }
                event.blockList().removeAll(toRemove);
            }
        }
    }
}
