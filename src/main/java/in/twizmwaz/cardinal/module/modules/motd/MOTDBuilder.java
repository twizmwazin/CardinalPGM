package in.twizmwaz.cardinal.module.modules.motd;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class MOTDBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<MOTD> results = new ModuleCollection<MOTD>();
        if (Cardinal.getInstance().getConfig().getBoolean("custom-motd")) results.add(new MOTD(match));
        return results;
    }

}
