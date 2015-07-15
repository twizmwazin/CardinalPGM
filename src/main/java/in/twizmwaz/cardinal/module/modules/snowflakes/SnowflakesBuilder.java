package in.twizmwaz.cardinal.module.modules.snowflakes;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class SnowflakesBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Snowflakes> load(Match match) {
        return new ModuleCollection<>(new Snowflakes());
    }

}
