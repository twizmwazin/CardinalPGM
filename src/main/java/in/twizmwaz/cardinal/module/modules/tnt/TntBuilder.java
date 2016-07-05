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
        if (match.getDocument().getRootElement().getChild("tnt") != null) {
            for (Element element : match.getDocument().getRootElement().getChildren("tnt")) {
                boolean instantIgnite = Numbers.parseBoolean(element.getChildText("instantignite"), false);
                boolean blockDamage = Numbers.parseBoolean(element.getChildText("blockdamage"), true);
                double yield = Numbers.limitDouble(0, 1, Numbers.parseDouble(element.getChildText("yield"), 0.3));
                double power = Numbers.parseDouble(element.getChildText("power"), 4.0);
                double fuse = element.getChild("fuse") != null ? Strings.timeStringToSeconds(element.getChildText("fuse")) : 4;
                int limit = Numbers.parseInt(element.getChildText("dispenser-tnt-limit"),16);
                double multiplier = Numbers.parseDouble(element.getChildText("dispenser-tnt-multiplier"), 0.25);
                results.add(new Tnt(instantIgnite, blockDamage, yield, power, fuse, limit, multiplier));
            }
        } else results.add(new Tnt(false, true, 0.3, 4.0, 4, 16, 0.25));
        return results;
    }

}
