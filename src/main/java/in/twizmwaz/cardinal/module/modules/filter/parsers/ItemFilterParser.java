package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.bukkit.Material;
import org.jdom2.Element;

public class ItemFilterParser extends FilterParser {

    private Material material;

    public ItemFilterParser(final Element element) {
        super(element);
        material = Material.matchMaterial(element.getChild("item").getText());
    }

    public Material getMaterial() {
        return material;
    }
}
