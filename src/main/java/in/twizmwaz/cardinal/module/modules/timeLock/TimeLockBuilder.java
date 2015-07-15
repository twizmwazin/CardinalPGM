package in.twizmwaz.cardinal.module.modules.timeLock;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.util.Numbers;
import org.jdom2.Element;

public class TimeLockBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TimeLock> load(Match match) {
        ModuleCollection<TimeLock> results = new ModuleCollection<>();
        Element timeLock = match.getDocument().getRootElement().getChild("timelock");
        if (timeLock != null && Numbers.parseBoolean(timeLock.getText())) {
            results.add(new TimeLock());
        }
        return results;
    }

}
