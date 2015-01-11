package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import in.twizmwaz.cardinal.filter.parsers.ItemFilterParser;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class WearingFilter extends Filter {

    private final Material material;

    public WearingFilter(final Material material) {
        this.material = material;
    }
    
    public WearingFilter(final ItemFilterParser parser) {
        this.material = parser.getMaterial();
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof PlayerEvent) {
            for (ItemStack armor : ((PlayerEvent) event).getPlayer().getInventory().getArmorContents()) {
                if (armor.getType().equals(material)) return ALLOW;
            }
            return DENY;
        } else return ABSTAIN;
    }

}
