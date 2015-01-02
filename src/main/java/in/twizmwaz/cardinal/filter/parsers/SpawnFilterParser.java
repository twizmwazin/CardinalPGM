package in.twizmwaz.cardinal.filter.parsers;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jdom2.Element;

public class SpawnFilterParser {

    private final CreatureSpawnEvent.SpawnReason reason;

    public SpawnFilterParser(final Element element) {
        this.reason = CreatureSpawnEvent.SpawnReason.valueOf(element.getText().toUpperCase().replace(" ", "_"));
    }

    public CreatureSpawnEvent.SpawnReason getReason() {
        return reason;
    }

}
