package in.twizmwaz.cardinal.module.modules.timeLock;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class TimeLockBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        try {
            if (match.getDocument().getRootElement().getChild("timelock").getText().equalsIgnoreCase("on")) {
                results.add(new TimeLock());
            }
        } catch (NullPointerException e) {

        }
        return results;
    }

}
