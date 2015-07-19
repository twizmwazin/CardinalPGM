package in.twizmwaz.cardinal.module.modules.worldBorder;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.util.DomUtils;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.ParseUtils;
import in.twizmwaz.cardinal.util.StringUtils;
import org.jdom2.Element;

import java.util.logging.Filter;

public class WorldBorderModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<WorldBorderModule> results = new ModuleCollection<>();
        for (Element borders : match.getDocument().getRootElement().getChildren("world-borders")) {
            for (Element border : borders.getChildren("world-border")) {
                double x = 0;
                double z = 0;
                if (border.getAttributeValue("center") != null) {
                    String center = border.getAttributeValue("center");
                    x = NumUtils.parseDouble(center.split(",")[0].trim());
                    z = NumUtils.parseDouble(center.split(",")[1].trim());
                } else {
                    String center = border.getParentElement().getAttributeValue("center");
                    x = NumUtils.parseDouble(center.split(",")[0].trim());
                    z = NumUtils.parseDouble(center.split(",")[1].trim());
                }

                double size = 0;
                if (border.getAttributeValue("size") != null) {
                    size = NumUtils.parseDouble(border.getAttributeValue("size").trim());
                } else {
                    size = NumUtils.parseDouble(border.getParentElement().getAttributeValue("size").trim());
                }

                FilterModule when = null;
                if (border.getChildren("when") != null) {
                    for (Element filter : border.getChildren("when")) {
                        when = FilterModuleBuilder.getFilter(filter);
                    }
                }

                int after = 0;
                if (border.getAttributeValue("after") != null) {
                    after = StringUtils.timeStringToSeconds(border.getAttributeValue("after"));
                } else if (border.getParentElement().getAttributeValue("after") != null) {
                    after = StringUtils.timeStringToSeconds(border.getParentElement().getAttributeValue("after"));
                }

                long duration = 0;
                if (border.getAttributeValue("duration") != null) {
                    duration = StringUtils.timeStringToSeconds(border.getAttributeValue("duration"));
                } else if (border.getParentElement().getAttributeValue("duration") != null) {
                    duration = StringUtils.timeStringToSeconds(border.getParentElement().getAttributeValue("duration"));
                }

                double damage = 0.2;
                if (border.getAttributeValue("damage") != null) {
                    damage = NumUtils.parseDouble(border.getAttributeValue("damage"));
                } else if (border.getParentElement().getAttributeValue("damage") != null) {
                    damage = NumUtils.parseDouble(border.getParentElement().getAttributeValue("damage"));
                }

                double buffer = 5;
                if (border.getAttributeValue("buffer") != null) {
                    buffer = NumUtils.parseDouble(border.getAttributeValue("buffer"));
                } else if (border.getParentElement().getAttributeValue("buffer") != null) {
                    buffer = NumUtils.parseDouble(border.getParentElement().getAttributeValue("buffer"));
                }

                int warningDistance = 5;
                if (border.getAttributeValue("warning-distance") != null) {
                    warningDistance = NumUtils.parseInt(border.getAttributeValue("warning-distance"));
                } else if (border.getParentElement().getAttributeValue("warning-distance") != null) {
                    warningDistance = NumUtils.parseInt(border.getParentElement().getAttributeValue("warning-distance"));
                }

                int warningTime = 0;
                if (border.getAttributeValue("warning-time") != null) {
                    warningTime = StringUtils.timeStringToSeconds(border.getAttributeValue("warning-time"));
                } else if (border.getParentElement().getAttributeValue("warning-time") != null) {
                    warningTime = StringUtils.timeStringToSeconds(border.getParentElement().getAttributeValue("warning-time"));
                }

                results.add(new WorldBorderModule(x, z, size, when, after, duration, damage, buffer, warningDistance, warningTime));
            }
        }
        return results;
    }

}
