package in.twizmwaz.cardinal.module.modules.proximityAlarm;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.ChatColor;
import org.jdom2.Element;

public class ProximityAlarmBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ProximityAlarm> load(Match match) {
        ModuleCollection<ProximityAlarm> results = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("proximity-alarms")) {
            for (Element subElement : element.getChildren("proximity-alarm")) {
                String message = null;
                if (subElement.getAttributeValue("message") != null) {
                    message = subElement.getAttributeValue("message");
                } else if (element.getAttributeValue("message") != null) {
                    message = element.getAttributeValue("message");
                }
                if (message != null) message = ChatColor.translateAlternateColorCodes('`', message);
                int flareRadius = 4;
                if (subElement.getAttributeValue("flare-radius") != null) {
                    flareRadius = Numbers.parseInt(subElement.getAttributeValue("flare-radius"));
                } else if (element.getAttributeValue("flare-radius") != null) {
                    flareRadius = Numbers.parseInt(element.getAttributeValue("flare-radius"));
                }
                RegionModule region = null;
                if (subElement.getChild("region") != null) {
                    region = RegionModuleBuilder.getRegion(subElement.getChild("region"));
                } else if (subElement.getAttributeValue("region") != null) {
                    region = RegionModuleBuilder.getRegion(subElement.getAttributeValue("region"));
                }
                FilterModule detect = null;
                if (subElement.getChild("detect") != null) {
                    detect = FilterModuleBuilder.getFilter(subElement.getChild("detect").getChildren().get(0));
                }
                FilterModule notify = null;
                if (subElement.getChild("notify") != null) {
                    notify = FilterModuleBuilder.getFilter(subElement.getChild("notify").getChildren().get(0));
                }
                results.add(new ProximityAlarm(message, flareRadius, region, detect, notify));
            }
        }
        return results;
    }

}
