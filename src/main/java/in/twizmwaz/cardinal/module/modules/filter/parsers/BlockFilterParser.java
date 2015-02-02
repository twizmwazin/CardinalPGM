package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.bukkit.Material;
import org.jdom2.Element;

public class BlockFilterParser extends FilterParser {

    private final Material material;
    private final int damageValue;

    public BlockFilterParser(final Element element) {
        super(element);
        if (element.getText().contains(":")) {
            this.material = Material.matchMaterial(element.getText().split(":")[0]);
            this.damageValue = Integer.parseInt(element.getText().split(":")[1]);
        } else {
            this.material = Material.matchMaterial(element.getText());
            this.damageValue = 0;
        }
    }

    public Material getMaterial() {
        return material;
    }

    public int getDamageValue() {
        return damageValue;
    }
}
