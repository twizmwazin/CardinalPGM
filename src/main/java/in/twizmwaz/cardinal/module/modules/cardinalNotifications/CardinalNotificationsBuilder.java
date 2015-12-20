package in.twizmwaz.cardinal.module.modules.cardinalNotifications;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class CardinalNotificationsBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<CardinalNotifications> load(Match match) {
        return new ModuleCollection<>(new CardinalNotifications());
    }

}