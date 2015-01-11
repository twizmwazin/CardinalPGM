package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class DestroyableObjectiveBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>();
        for (Element destroyablesElement : match.getDocument().getRootElement().getChildren("destroyables")) {
            for (Element element : destroyablesElement.getChildren("destroyable")) {

            }
        }
        return results;
    }

}
