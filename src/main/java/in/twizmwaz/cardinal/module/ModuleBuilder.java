package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.match.Match;

public interface ModuleBuilder {

    ModuleCollection<? extends Module> load(Match match);

}
