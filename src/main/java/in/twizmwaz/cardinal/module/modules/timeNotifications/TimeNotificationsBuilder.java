package in.twizmwaz.cardinal.module.modules.timeNotifications;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;

@LoadTime(ModuleLoadTime.LATEST)
public class TimeNotificationsBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TimeNotifications> load(Match match) {
        return new ModuleCollection<>(new TimeNotifications());
    }

}
