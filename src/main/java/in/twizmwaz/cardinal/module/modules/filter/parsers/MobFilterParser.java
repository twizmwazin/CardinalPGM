package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.bukkit.entity.CreatureType;
import org.jdom2.Element;

public class MobFilterParser extends FilterParser {

    private final CreatureType mobType;

    public MobFilterParser(final Element element) {
        super(element);
        this.mobType = CreatureType.valueOf(element.getText().toUpperCase().replace(" ", "_"));
    }

    public CreatureType getMobType() {
        return mobType;
    }
}
