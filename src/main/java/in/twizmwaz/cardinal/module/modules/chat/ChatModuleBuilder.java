package in.twizmwaz.cardinal.module.modules.chat;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class ChatModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<ChatModule> results = new ModuleCollection<ChatModule>();
        
        results.add(new ChatModule());
        return results;
    }
}
