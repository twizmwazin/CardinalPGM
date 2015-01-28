package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class VelocityRegion extends AppliedRegion {
    
    public final Vector velocity;
    
    public VelocityRegion(RegionModule region, Vector velocity) {
        super(region);
        this.velocity = velocity;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (getRegion().contains(new BlockRegion(null, event.getTo().toVector())) && event.getPlayer().isOnGround())
            event.getPlayer().setVelocity(velocity);
    }
}
