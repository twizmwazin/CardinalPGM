package in.twizmwaz.cardinal.module.modules.difficulty;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import org.bukkit.Difficulty;

import java.util.ArrayList;
import java.util.List;

public class MapDifficultyBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        try {
            Difficulty difficulty;

            switch (Integer.parseInt(match.getDocument().getRootElement().getChildText("difficulty"))) {
                case 0:
                    results.add(new MapDifficulty(Difficulty.PEACEFUL));
                    break;
                case 1:
                    results.add(new MapDifficulty(Difficulty.EASY));
                    break;
                case 2:
                    results.add(new MapDifficulty(Difficulty.NORMAL));
                    break;
                case 3:
                    results.add(new MapDifficulty(Difficulty.HARD));
                    break;
            }
        } catch (NumberFormatException e) {

        }
        return results;
    }
}
