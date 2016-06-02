package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ItemFilterParser;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class CarryingFilter extends FilterModule {

    private final ItemStack item;

    public CarryingFilter(final ItemFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.item = parser.getItem();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof InventoryHolder) {
                for (ItemStack item : ((InventoryHolder) object).getInventory()) {
                    if (item != null && this.item.isSimilar(item))
                        return ALLOW;
                }
                return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
