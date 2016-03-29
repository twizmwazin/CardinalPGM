package in.twizmwaz.cardinal.module.modules.itemMods;


import in.twizmwaz.cardinal.util.Parser;
import org.bukkit.inventory.ItemStack;
import org.jdom2.Element;

public class ItemRule {

    private final ItemMatch match;
    private final Element modify;

    protected ItemRule(ItemMatch match, Element modify) {
        this.match = match;
        this.modify = modify;
    }

    protected void apply(ItemStack itemStack) {
        if (match.match(itemStack)) {
            Parser.applyMeta(itemStack, modify);
        }
    }

}
