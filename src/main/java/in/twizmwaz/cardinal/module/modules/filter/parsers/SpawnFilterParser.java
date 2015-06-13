package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jdom2.Element;

public class SpawnFilterParser extends FilterParser {

    private final CreatureSpawnEvent.SpawnReason reason;

    public SpawnFilterParser(final Element element) {
        super(element);
        this.reason = CreatureSpawnEvent.SpawnReason.valueOf(element.getText().toUpperCase().replace(" ", "_"));
    }

    public CreatureSpawnEvent.SpawnReason getReason() {
        return reason;
    }

}
