package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ItemFilterParser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class CarryingFilter extends FilterModule {

    private final Material material;

    public CarryingFilter(final ItemFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.material = parser.getMaterial();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Player) {
                for (ItemStack item : ((Player) object).getInventory()) {
                    if (item.getType().equals(material))
                        return getParent() == null ? ALLOW : (getParent().evaluate(objects).equals(DENY) ? DENY : ALLOW);
                }
                return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
