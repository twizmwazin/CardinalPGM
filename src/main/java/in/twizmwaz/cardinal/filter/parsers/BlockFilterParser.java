package in.twizmwaz.cardinal.filter.parsers;

import org.bukkit.Material;
import org.jdom2.Element;

public class BlockFilterParser {

    private final Material material;

    public BlockFilterParser(final Element element) {
        this.material = Material.matchMaterial(element.getText());
    }

    public Material getMaterial() {
        return material;
    }
}
