package in.twizmwaz.cardinal.module.modules.antimob;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;

public class AntiMobModule implements Module {

    private List<EntityType> types = Arrays.asList(EntityType.PAINTING, EntityType.ARMOR_STAND, EntityType.ARROW, EntityType.ITEM_FRAME, EntityType.BOAT, EntityType.EGG, EntityType.FALLING_BLOCK, EntityType.ENDER_CRYSTAL, EntityType.EXPERIENCE_ORB, EntityType.FISHING_HOOK, EntityType.DROPPED_ITEM, EntityType.LEASH_HITCH, EntityType.SPLASH_POTION, EntityType.PLAYER, EntityType.PRIMED_TNT, EntityType.THROWN_EXP_BOTTLE);

    public AntiMobModule(Match match) {
        for (Entity entity : GameHandler.getGameHandler().getMatchWorld().getEntities()) {
            if (!types.contains(entity.getType())) {
                entity.remove();
            }
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

}
