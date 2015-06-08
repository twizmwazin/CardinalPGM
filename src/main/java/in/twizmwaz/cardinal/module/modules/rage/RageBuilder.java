package in.twizmwaz.cardinal.module.modules.rage;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.LATER)
public class RageBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for (Element element : match.getDocument().getRootElement().getChildren("rage")) {
            results.add(new Rage());
        }
        return results;
    }
}


