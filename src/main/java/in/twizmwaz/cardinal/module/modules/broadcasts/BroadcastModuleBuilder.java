package in.twizmwaz.cardinal.module.modules.broadcasts;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.broadcasts.BroadcastModule.BroadcastType;
import in.twizmwaz.cardinal.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.Element;

public class BroadcastModuleBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection result = new ModuleCollection();
        for (Element broadcast : match.getDocument().getRootElement().getChildren("broadcasts")) {
            for (Element element : broadcast.getChildren("tip")) {
                String message;
                int timeAfter;
                int every;
                int count = 1;
                message = element.getText();
                timeAfter = StringUtils.timeStringToSeconds(element.getAttributeValue("after"));
                if (element.getAttributeValue("every") != null) {
                    every = StringUtils.timeStringToSeconds(element.getAttributeValue("every"));
                } else {
                    every = timeAfter;
                }
                if (element.getAttributeValue("count") != null) {
                    count = Integer.parseInt(element.getAttributeValue("count"));
                }
                new BroadcastModule(message, BroadcastType.TIP, timeAfter, every, count);
            }
            for (Element element : broadcast.getChildren("alert")) {
                String message;
                int timeAfter;
                int every;
                int count = 1;
                message = element.getText();
                timeAfter = StringUtils.timeStringToSeconds(element.getAttributeValue("after"));
                if (element.getAttributeValue("every") != null) {
                    every = StringUtils.timeStringToSeconds(element.getAttributeValue("every"));
                } else {
                    every = timeAfter;
                }
                if (element.getAttributeValue("count") != null) {
                    count = Integer.parseInt(element.getAttributeValue("count"));
                }
                new BroadcastModule(message, BroadcastType.ALERT, timeAfter, every, count);
            }
        }
        return result;
    }
}
