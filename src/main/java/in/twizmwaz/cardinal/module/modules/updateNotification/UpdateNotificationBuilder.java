package in.twizmwaz.cardinal.module.modules.updateNotification;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class UpdateNotificationBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<UpdateNotification> load(Match match) {
        return new ModuleCollection<>(new UpdateNotification());
    }

}