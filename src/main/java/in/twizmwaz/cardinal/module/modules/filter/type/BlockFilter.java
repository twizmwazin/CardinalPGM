package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.BlockFilterParser;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class BlockFilter extends FilterModule {

    private final Material material;
    private final int damageValue;

    public BlockFilter(final BlockFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.material = parser.getMaterial();
        this.damageValue = parser.getDamageValue();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Block) {
                if (((Block) object).getType().equals(material) && (damageValue == -1 || (int) ((Block) object).getState().getData().getData() == damageValue))
                    return ALLOW;
                else return DENY;
            } else if (object instanceof Material) {
                if ((object).equals(material))
                    return ALLOW;
                else return DENY;
            } else if (object instanceof MaterialData) {
                if (((MaterialData) object).getItemType().equals(material) && (damageValue == -1 || (int) ((MaterialData) object).getData() == damageValue))
                    return ALLOW;
                else return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }
}