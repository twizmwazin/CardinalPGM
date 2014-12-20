package in.twizmwaz.cardinal.module.modules.timeLock;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;

import java.util.ArrayList;
import java.util.List;

public class TimeLockBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>();
        try {
            if (match.getDocument().getRootElement().getChild("timelock").getText().equalsIgnoreCase("on")) {
                results.add(new TimeLock());
            }
        } catch (NullPointerException e) {

        }
        return results;
    }

}
