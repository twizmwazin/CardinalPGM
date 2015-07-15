package in.twizmwaz.cardinal.module.modules.tnt;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Strings;
import org.jdom2.Element;

public class TntBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Tnt> load(Match match) {
        ModuleCollection<Tnt> results = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("tnt")) {
            boolean instantIgnite = false;
            if (element.getChild("instantignite") != null) {
                instantIgnite = Numbers.parseBoolean(element.getChildText("instantignite"));
            }
            boolean blockDamage = true;
            if (element.getChild("blockdamage") != null) {
                blockDamage = Numbers.parseBoolean(element.getChildText("blockdamage"));
            }
            double yield = 0.3;
            if (element.getChild("yield") != null) {
                yield = Double.parseDouble(element.getChildText("yield"));
            }
            double power = 4.0;
            if (element.getChild("power") != null) {
                power = Double.parseDouble(element.getChildText("power"));
            }
            int fuse = 4;
            if (element.getChild("fuse") != null) {
                fuse = Strings.timeStringToSeconds(element.getChildText("fuse"));
            }
            results.add(new Tnt(instantIgnite, blockDamage, yield, power, fuse));
        }
        return results;
    }

}
