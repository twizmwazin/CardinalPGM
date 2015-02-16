package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.bukkit.entity.EntityType;
import org.jdom2.Element;

public class EntityFilterParser extends FilterParser {

    private final EntityType entityType;

    public EntityFilterParser(final Element element) {
        super(element);
        this.entityType = EntityType.valueOf(element.getText().toUpperCase().replace(" ", "_"));
    }

    public EntityType getEntityType() {
        return entityType;
    }

}
