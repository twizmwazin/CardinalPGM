package in.twizmwaz.cardinal.module.modules.potionRemover;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class PotionRemoverBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection<PotionRemover>();
        if (match.getDocument().getRootElement().getChild("keep-potion-bottles") == null) {
            results.add(new PotionRemover());
        }
        return results;
    }

}