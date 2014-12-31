package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class CarryingFilter extends Filter {

    private final Material material;

    public CarryingFilter(final Material material) {
        this.material = material;
    }

    @Override
    public FilterState getState(final Object o) {
        if (o instanceof Player) {
            for (ItemStack item : ((Player) o).getInventory()) {
                if (item.getType().equals(material)) return ALLOW;
            }
            return DENY;
        } else return ABSTAIN;
    }

}
