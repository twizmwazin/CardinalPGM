package in.twizmwaz.cardinal.module.modules.guiKeep;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class GuiKeepModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<GuiKeepModule> load(Match match) {
        return new ModuleCollection<>(new GuiKeepModule());
    }

}
