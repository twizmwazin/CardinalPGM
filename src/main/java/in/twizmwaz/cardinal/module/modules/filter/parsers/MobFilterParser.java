package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.bukkit.entity.EntityType;
import org.jdom2.Element;

public class MobFilterParser extends FilterParser {

    private final EntityType mobType;

    public MobFilterParser(final Element element) {
        super(element);
        this.mobType = EntityType.valueOf(element.getText().toUpperCase().replace(" ", "_"));
    }

    public EntityType getMobType() {
        return mobType;
    }
}
