package in.twizmwaz.cardinal.module.modules.teamPicker;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class TeamPickerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TeamPicker> load(Match match) {
        return new ModuleCollection<>(new TeamPicker());
    }

}
