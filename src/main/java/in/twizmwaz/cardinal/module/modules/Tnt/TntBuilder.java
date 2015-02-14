package in.twizmwaz.cardinal.module.modules.Tnt;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.blockdrops.Blockdrops;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.StringUtils;
import org.jdom2.Element;

public class TntBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for (Element element : match.getDocument().getRootElement().getChildren("tnt")) {
            boolean instantIgnite = false;
            if (element.getChild("instantignite") != null) {
                instantIgnite = NumUtils.parseBoolean(element.getChildText("instantignite"));
            }
            boolean blockDamage = true;
            if (element.getChild("blockdamage") != null) {
                blockDamage = NumUtils.parseBoolean(element.getChildText("blockdamage"));
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
                fuse = StringUtils.timeStringToSeconds(element.getChildText("fuse"));
            }
            results.add(new Tnt(instantIgnite, blockDamage, yield, power, fuse));
        }
        return results;
    }

}
