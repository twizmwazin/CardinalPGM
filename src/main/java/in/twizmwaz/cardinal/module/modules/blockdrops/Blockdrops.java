package in.twizmwaz.cardinal.module.modules.blockdrops;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.kit.KitNode;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Blockdrops implements Module {

    private final RegionModule region;
    private final FilterModule filter;
    private final KitNode kit;
    private final Map<ItemStack, Double> drops;
    private final Material replaceType;
    private final int replaceDamage, experience;
    private final boolean wrongTool, punch, trample;
    private final double fallChance, landChance, fallSpeed;

    protected Blockdrops(final RegionModule region, final FilterModule filter, final KitNode kit, final Map<ItemStack, Double> drops, final Material replaceType, final int replaceDamage, final int experience, final boolean wrongTool, final boolean punch, final boolean trample, final double fallChance, final double landChance, final double fallSpeed) {
        this.region = region;
        this.filter = filter;
        this.kit = kit;
        this.drops = drops;
        this.replaceType = replaceType;
        this.replaceDamage = replaceDamage;
        this.experience = experience;
        this.wrongTool = wrongTool;
        this.punch = punch;
        this.trample = trample;
        this.fallChance = fallChance;
        this.landChance = landChance;
        this.fallSpeed = fallSpeed;
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
                        if (this.wrongTool || block.getDrops() != null &&  block.getDrops().size() > 0) {
                            generateDrops(player, block);
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
                            generateDrops(player, block);
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerAnimation(PlayerInteractEvent event) { // event not triggering with right click
        if (!event.isCancelled()) {
            if (this.punch) {
                Bukkit.broadcastMessage("punch on");
                if (filter.evaluate(event).equals(FilterState.ALLOW)) { // should use punch
                    Bukkit.broadcastMessage("filter allow");
                    if (event.getPlayer().getGameMode() == GameMode.ADVENTURE) { // <cause>punch<cause> or <cause>mine<cause>
                        Bukkit.broadcastMessage("gamemode on");
                        if (region == null || region.contains(event.getClickedBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5)))) {
                            Bukkit.broadcastMessage("drops gen");
                            generateDrops(event.getPlayer(), event.getClickedBlock());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if (this.trample && !ObserverModule.testObserverOrDead(event.getPlayer())) {
            if (event.getTo().getBlock() != event.getFrom().getBlock()) {
                Block block = event.getTo().getBlock();
                Bukkit.broadcastMessage(block.getType().toString());
                if (filter.evaluate(event).equals(FilterState.ALLOW)) {
                    if (region == null || region.contains(block.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5)))) {
                        // do magic filter checking.. needs <sprinting/>, <walking/> & <crouching/> support.
                        generateDrops(event.getPlayer(), block);
                        if (this.replaceType != Material.AIR) {
                            block.setType(replaceType);
                            block.setData((byte) replaceDamage);
                        }
                    }
                }
            }
        }
    }

    public void generateDrops(Player player, Block block) {
        for (ItemStack drop : this.drops.keySet()) {
            if (drops.get(drop) == 1 || new Random().nextDouble() <= drops.get(drop)) {
                GameHandler.getGameHandler().getMatchWorld().dropItemNaturally(block.getLocation().clone().add(.5, .5, .5), drop);
            }
        }
        if (this.experience != 0) {
            ExperienceOrb xp = GameHandler.getGameHandler().getMatchWorld().spawn(block.getLocation(), ExperienceOrb.class);
            xp.setExperience(this.experience);
        }
        if (this.kit != null) {
            kit.apply(player, true); // should be true? forced?
        }
    }

}
