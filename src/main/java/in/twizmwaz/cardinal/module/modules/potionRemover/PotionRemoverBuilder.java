package in.twizmwaz.cardinal.module.modules.potionRemover;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class PotionRemoverBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<PotionRemover> load(Match match) {
        ModuleCollection<PotionRemover> results = new ModuleCollection<>();
        if (match.getDocument().getRootElement().getChild("keep-potion-bottles") == null) {
            results.add(new PotionRemover());
        }
        return results;
    }

}