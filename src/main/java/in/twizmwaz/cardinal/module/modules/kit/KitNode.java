package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class KitNode implements KitRemovable {

    private String preParents;
    private String preFilter;

    private String name;
    private FilterModule filter = null;
    private boolean force;
    private boolean potionParticles;
    private boolean discardPotionBottles;
    private boolean resetEnderPearls;

    private List<Kit> kits;
    private List<KitNode> parentKits = new ArrayList<>();

    protected KitNode(String name, String filter, boolean force, boolean potionParticles, boolean discardPotionBottles, boolean resetEnderPearls, List<Kit> kits, String parents) {
        this.name = name;
        this.force = force;
        this.potionParticles = potionParticles;
        this.discardPotionBottles = discardPotionBottles;
        this.resetEnderPearls = resetEnderPearls;
        this.kits = kits;

        this.preParents = parents;
        this.preFilter = filter;
    }

    public static KitNode getKitByName(String name) {
        for (KitNode kit : GameHandler.getGameHandler().getMatch().getModules().getModules(KitNode.class)) {
            if (kit.getName().equalsIgnoreCase(name)) return kit;
        }
        return null;
    }

    public List<Kit> getKits() {
        return this.kits;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        if (filter != null && filter.evaluate(player).equals(FilterState.DENY)) return;
        for (KitNode kit : parentKits) {
            kit.apply(player, force != null ? force : this.force);
        }
        for (Kit kit : kits) {
            kit.apply(player, force != null ? force : this.force);
        }
        player.setPotionParticles(potionParticles);
        if (discardPotionBottles) player.getInventory().remove(Material.GLASS_BOTTLE);
        if (resetEnderPearls) {
            for (Entity entity : GameHandler.getGameHandler().getMatchWorld().getEntities()) {
                if (entity instanceof EnderPearl) {
                    EnderPearl enderPearl = (EnderPearl) entity;
                    if (enderPearl.getShooter() != null && enderPearl.getShooter() instanceof Player && enderPearl.getShooter().equals(player)) {
                        enderPearl.setShooter(null);
                    }
                }
            }
        }
        player.updateInventory();
    }

    @Override
    public void remove(Player player) {
        for (Kit kit : kits) {
            if (kit instanceof KitRemovable) ((KitRemovable) kit).remove(player);
        }
        for (KitNode kit : parentKits) {
            kit.remove(player);
        }
        player.updateInventory();
    }

    public String getName() {
        return name;
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        if (!preParents.equals("")) {
            for (String parent : preParents.split(",")) {
                KitNode kit = getKitByName(parent);
                if (kit != null) parentKits.add(kit);
            }
        }
        filter = FilterModuleBuilder.getFilter(preFilter);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeamChange(PlayerQuitEvent event) {
        remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeamChange(PlayerChangeTeamEvent event) {
        remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCardinalDeath(CardinalDeathEvent event) {
        remove(event.getPlayer());
    }

}
