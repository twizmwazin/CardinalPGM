package in.twizmwaz.cardinal.module.modules.hunger;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class HungerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Hunger> load(Match match) {
        ModuleCollection<Hunger> results = new ModuleCollection<>();
        try {
            String data = match.getDocument().getRootElement().getChild("hunger").getChildText("depletion");
            if (data.equalsIgnoreCase("off")) {
                results.add(new Hunger(false));
            } else {
                results.add(new Hunger(true));
            }
        } catch (NullPointerException e) {
            results.add(new Hunger(true));
        }
        return results;
    }

}
