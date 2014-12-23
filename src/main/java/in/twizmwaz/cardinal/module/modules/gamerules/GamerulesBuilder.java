package in.twizmwaz.cardinal.module.modules.gamerules;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamerulesBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>();
        Set<String> toDisable = new HashSet<>(128);
        for (Element itemRemove : match.getDocument().getRootElement().getChildren("gamerules")) {
            for (Element item : itemRemove.getChildren()) {
                if (item.getText().equalsIgnoreCase("false")) {
                    toDisable.add(item.getName());
                }
            }
        }
        results.add(new Gamerules(toDisable));
        return results;
    }

}
