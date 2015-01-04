package in.twizmwaz.cardinal.filter.parsers;

import org.bukkit.Material;
import org.jdom2.Element;

public class ItemFilterParser {
    
    private Material material;
    
    public ItemFilterParser(final Element element) {
        material = Material.matchMaterial(element.getChild("item").getText());
    }

    public Material getMaterial() {
        return material;
    }
}
