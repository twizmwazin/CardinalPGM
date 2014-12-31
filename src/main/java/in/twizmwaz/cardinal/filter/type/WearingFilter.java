package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class WearingFilter extends Filter {

    private final Material material;

    public WearingFilter(final Material material) {
        this.material = material;
    }

    @Override
    public FilterState getState(final Object o) {
        if (o instanceof Player) {
            for (ItemStack armor : ((Player) o).getInventory().getArmorContents()) {
                if (armor.getType().equals(material)) return ALLOW;
            }
            return DENY;
        } else return ABSTAIN;
    }

}
