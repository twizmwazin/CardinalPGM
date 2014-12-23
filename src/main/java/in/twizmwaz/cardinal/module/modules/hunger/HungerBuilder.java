package in.twizmwaz.cardinal.module.modules.hunger;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;

import java.util.ArrayList;
import java.util.List;

public class HungerBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>(1);
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
