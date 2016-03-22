package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.KitRemovable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.List;

public class AttributeModifierKit implements KitRemovable {

    List<AttributeModifier> attributes;

    public AttributeModifierKit(List<AttributeModifier> attributes) {
        this.attributes = attributes;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        for (AttributeModifier modifier : attributes) {
            player.getAttribute(Attribute.byName(modifier.getName())).addModifier(modifier);
        }
    }

    @Override
    public void remove(Player player) {
        for (AttributeModifier modifier : attributes) {
            player.getAttribute(Attribute.byName(modifier.getName())).removeModifier(modifier);
        }
    }

}
