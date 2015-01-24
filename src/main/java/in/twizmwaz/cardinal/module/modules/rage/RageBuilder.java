package in.twizmwaz.cardinal.module.modules.rage;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import org.jdom2.Element;

public class RageBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for (Element element : match.getDocument().getRootElement().getChildren()){
            if (element.getName().equals("rage"))
                System.out.println("1");
                results.add(new Rage());
        }
        return results;
    }
}


