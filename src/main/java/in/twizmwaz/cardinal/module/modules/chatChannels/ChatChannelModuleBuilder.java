package in.twizmwaz.cardinal.module.modules.chatChannels;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;

public class ChatChannelModuleBuilder implements ModuleBuilder {
    
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<ChatChannelModule> results = new ModuleCollection<ChatChannelModule>();
        results.add(new GlobalChannel());
        results.add(new AdminChannel(match.getModules().getModule(PermissionModule.class)));
        for (TeamModule teamModule : match.getModules().getModules(TeamModule.class)) {
            results.add(new TeamChannel(teamModule, match.getModules().getModule(PermissionModule.class)));
        }
        return results;
    }
}
