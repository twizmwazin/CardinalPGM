package in.twizmwaz.cardinal.module.modules.motd;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.util.Config;

public class MOTDBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MOTD> load(Match match) {
        ModuleCollection<MOTD> results = new ModuleCollection<>();
        if (Config.customMotd) {
            results.add(new MOTD(match));
        }
        return results;
    }

}
