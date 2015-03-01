package in.twizmwaz.cardinal.module.modules.broadcasts;

import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.ChatColor;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.broadcasts.BroadcastModule.BroadcastType;
import in.twizmwaz.cardinal.util.StringUtils;
import org.jdom2.Element;

public class BroadcastModuleBuilder implements ModuleBuilder {
    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for (Element broadcast : match.getDocument().getRootElement().getChildren("broadcasts")) {
            for (Element element : broadcast.getChildren("tip")) {
                String message;
                int timeAfter;
                int every;
                int count = 1;
                message = ChatColor.translateAlternateColorCodes('`', element.getText());
                timeAfter = StringUtils.timeStringToSeconds(element.getAttributeValue("after"));
                if (element.getAttributeValue("every") != null) {
                    every = StringUtils.timeStringToSeconds(element.getAttributeValue("every"));
                } else {
                    every = timeAfter;
                }
                if (element.getAttributeValue("count") != null) {
                    count = NumUtils.parseInt(element.getAttributeValue("count"));
                }
                results.add(new BroadcastModule(message, BroadcastType.TIP, timeAfter, every, count));
            }
            for (Element element : broadcast.getChildren("alert")) {
                String message;
                int timeAfter;
                int every;
                int count = Integer.MAX_VALUE;
                message = ChatColor.translateAlternateColorCodes('`', element.getText());
                timeAfter = StringUtils.timeStringToSeconds(element.getAttributeValue("after"));
                if (element.getAttributeValue("every") != null) {
                    every = StringUtils.timeStringToSeconds(element.getAttributeValue("every"));
                } else {
                    every = timeAfter;
                }
                if (element.getAttributeValue("count") != null) {
                    count = NumUtils.parseInt(element.getAttributeValue("count"));
                }
                results.add(new BroadcastModule(message, BroadcastType.ALERT, timeAfter, every, count));
            }
        }
        return results;
    }
}
