package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class KitCollection implements KitRemovable {

    private String name;
    private FilterModule filter;
    private Boolean force;
    private Boolean potionParticles;
    private Boolean discardPotionBottles;
    private Boolean resetEnderPearls;
    private String parents;

    private List<Kit> kits;
    private List<KitCollection> parentKits = new ArrayList<>();

    protected KitCollection(String name, FilterModule filter, Boolean force, Boolean potionParticles, Boolean discardPotionBottles, Boolean resetEnderPearls, List<Kit> kits, String parents) {
        this.name = name;
        this.filter = filter;
        this.force = force;
        this.potionParticles = potionParticles;
        this.discardPotionBottles = discardPotionBottles;
        this.resetEnderPearls = resetEnderPearls;
        this.kits = kits;
        this.parents = parents;
    }

    public static KitCollection getKitByName(String name) {
        for (KitCollection kit : GameHandler.getGameHandler().getMatch().getModules().getModules(KitCollection.class)) {
            if (kit.getName().equalsIgnoreCase(name)) return kit;
        }
        return null;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        // Filter will always be null, kits get loaded before them
        // if (filter.evaluate(player).equals(FilterState.DENY)) return;
        for (KitCollection kit : parentKits) {
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
    }

    @Override
    public void remove(Player player) {
        for (Kit kit : kits) {
            if (kit instanceof KitRemovable) ((KitRemovable) kit).remove(player);
        }
        for (KitCollection kit : parentKits) {
            kit.remove(player);
        }
    }

    public String getName() {
        return name;
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        if (!parents.equals("")) {
            for (String parent : parents.split(",")) {
                KitCollection kit = getKitByName(parent);
                if (kit != null) parentKits.add(kit);
            }
        }
    }

}
