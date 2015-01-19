package in.twizmwaz.cardinal.module.modules.tasker;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class TaskerModuleBuilder implements ModuleBuilder {
    
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        results.add(new TaskerModule(match));
        return results;
    }
}
