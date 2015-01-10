package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class CarryingFilter extends Filter {

    private final Material material;

    public CarryingFilter(final Material material) {
        this.material = material;
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof PlayerEvent) {
            for (ItemStack item : ((PlayerEvent) event).getPlayer().getInventory()) {
                if (item.getType().equals(material)) return ALLOW;
            }
            return DENY;
        } else return ABSTAIN;
    }

}
