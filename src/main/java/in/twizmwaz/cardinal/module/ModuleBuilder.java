package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.match.Match;

import java.util.List;

public interface ModuleBuilder {

    public List<Module> load(Match match);

}
