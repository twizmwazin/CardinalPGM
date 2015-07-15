package in.twizmwaz.cardinal.module.modules.mapNotification;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class MapNotificationBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MapNotification> load(Match match) {
        return new ModuleCollection<>(new MapNotification());
    }
}
