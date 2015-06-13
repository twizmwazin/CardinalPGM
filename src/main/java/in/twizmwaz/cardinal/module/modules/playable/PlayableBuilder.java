package in.twizmwaz.cardinal.module.modules.playable;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.NegativeRegion;
import org.jdom2.Element;

public class PlayableBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        RegionModule region = null;
        for (Element element : match.getDocument().getRootElement().getChildren("playable")) {
            if (element != null) region = new NegativeRegion(new CombinationParser(element, match.getDocument()));
        }
        results.add(new Playable(region));
        return results;
    }
}
