package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.bukkit.Material;
import org.jdom2.Element;

public class BlockFilterParser extends FilterParser {

    private final Material material;

    public BlockFilterParser(final Element element) {
        super(element);
        this.material = Material.matchMaterial(element.getText());
    }

    public Material getMaterial() {
        return material;
    }
}
