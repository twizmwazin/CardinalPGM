package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.util.Parser;
import org.bukkit.inventory.ItemStack;
import org.jdom2.Element;

public class ItemFilterParser extends FilterParser {

    private ItemStack item;

    public ItemFilterParser(final Element element) {
        super(element);
        item = Parser.getItem(element.getChild("item"));
    }

    public ItemStack getItem() {
        return item;
    }
}
