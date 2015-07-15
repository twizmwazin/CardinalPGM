package in.twizmwaz.cardinal.module.modules.matchTranscript;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class MatchTranscriptBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MatchTranscript> load(Match match) {
        return new ModuleCollection<>(new MatchTranscript());
    }
}
