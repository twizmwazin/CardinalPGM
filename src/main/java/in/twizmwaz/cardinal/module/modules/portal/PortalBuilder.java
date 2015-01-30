package in.twizmwaz.cardinal.module.modules.portal;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import org.jdom2.Element;

public class PortalBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<Portal> results = new ModuleCollection<>();
        for (Element portals : match.getDocument().getRootElement().getChildren("portals")) {
            for (Element portal : portals.getChildren("portal")) {

            }
        }
        return results;
    }

}
