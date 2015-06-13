package in.twizmwaz.cardinal.module.modules.matchTranscript;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class MatchTranscriptBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        results.add(new MatchTranscript());
        return results;
    }
}
