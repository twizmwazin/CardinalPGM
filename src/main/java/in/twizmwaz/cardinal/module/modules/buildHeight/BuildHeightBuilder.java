package in.twizmwaz.cardinal.module.modules.buildHeight;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

public class BuildHeightBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<BuildHeight> result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("maxbuildheight")) {
            int height = NumUtils.parseInt(element.getValue());
            result.add(new BuildHeight(height));
        }
        return result;
    }

}
