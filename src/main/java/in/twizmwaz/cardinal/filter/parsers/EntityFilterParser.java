package in.twizmwaz.cardinal.filter.parsers;

import org.bukkit.entity.EntityType;
import org.jdom2.Element;

public class EntityFilterParser {

    private final EntityType mobType;

    public EntityFilterParser(final Element element) {
        this.mobType = EntityType.valueOf(element.getText().toUpperCase().replace(" ", "_"));
    }

    public EntityType getMobType() {
        return mobType;
    }

}
