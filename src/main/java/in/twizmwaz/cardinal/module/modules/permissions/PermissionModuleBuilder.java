package in.twizmwaz.cardinal.module.modules.permissions;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class PermissionModuleBuilder implements ModuleBuilder {
    
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<PermissionModule> results = new ModuleCollection<PermissionModule>();
        results.add(new PermissionModule(GameHandler.getGameHandler().getPlugin()));
        return results;
    }
}
