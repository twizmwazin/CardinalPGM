package in.twizmwaz.cardinal.module.modules.proximityAlarm;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import org.bukkit.Bukkit;
import org.jdom2.Element;

import java.util.logging.Level;

public class ProximityAlarmBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for (Element element : match.getDocument().getRootElement().getChildren("proximity-alarms")) {
            for (Element subElement : element.getChildren("proximity-alarm")) {
                String message = null;
                if (subElement.getAttributeValue("message") != null) {
                    message = subElement.getAttributeValue("message");
                } else if (element.getAttributeValue("message") != null) {
                    message = element.getAttributeValue("message");
                }
                int flareradius = 4;
                if (subElement.getAttributeValue("flare-radius") != null) {
                    flareradius = Integer.parseInt(subElement.getAttributeValue("flare-radius"));
                } else if (element.getAttributeValue("flare-radius") != null) {
                    flareradius = Integer.parseInt(element.getAttributeValue("flare-radius"));
                }
                RegionModule region = null;
                if (subElement.getChild("region") != null) {
                    region = RegionModuleBuilder.getRegion(subElement.getChild("region"));
                } else if (subElement.getAttributeValue("region") != null) {
                    region = RegionModuleBuilder.getRegion(subElement.getAttributeValue("region"));
                    Bukkit.getLogger().log(Level.INFO, "BUILDER: REGION " + subElement.getAttributeValue("region"));
                }
                FilterModule detect = null;
                if (subElement.getChild("detect") != null) {
                    detect = FilterModuleBuilder.getFilter(subElement.getChild("detect").getChildren().get(0));
                }
                FilterModule notify = null;
                if (subElement.getChild("notify") != null) {
                    notify = FilterModuleBuilder.getFilter(subElement.getChild("notify").getChildren().get(0));
                }
                results.add(new ProximityAlarm(message, flareradius, region, detect, notify));
            }
        }
        return results;
    }

}
