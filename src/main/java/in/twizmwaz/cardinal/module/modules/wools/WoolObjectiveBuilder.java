package in.twizmwaz.cardinal.module.modules.wools;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.DyeColor;
import org.jdom2.Element;

public class WoolObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<WoolObjective> result = new ModuleCollection<WoolObjective>();
        for (Element element : match.getDocument().getRootElement().getChildren("wools")) {
            for (Element subElement : element.getChildren("wool")) {
                TeamModule team;
                try {
                    team = TeamUtils.getTeamById(element.getAttributeValue("team"));
                } catch (NullPointerException e) {
                    team = TeamUtils.getTeamById((subElement.getAttributeValue("team")));
                }
                DyeColor color = StringUtils.convertStringToDyeColor(subElement.getAttributeValue("color"));
                BlockRegion place = RegionModuleBuilder.getRegion(subElement.getChildren().get(0)).getCenterBlock();
                String name = color == null ? "Wool" : color.name() + " Wool";
                if (element.getAttributeValue("name") != null) {
                    name = subElement.getAttributeValue("name");
                }
                String id = subElement.getAttributeValue("id");
                if (id == null) {
                    id = subElement.getAttributeValue("color");
                }
                boolean craftable = true;
                if (subElement.getAttributeValue("craftable") != null) {
                    craftable = !subElement.getAttributeValue("craftable").equalsIgnoreCase("false");
                } else if (element.getAttributeValue("craftable") != null) {
                    craftable = !element.getAttributeValue("craftable").equalsIgnoreCase("false");
                }
                boolean show = true;
                if (subElement.getAttributeValue("show") != null) {
                    show = !subElement.getAttributeValue("show").equalsIgnoreCase("false");
                } else if (element.getAttributeValue("show") != null) {
                    show = !element.getAttributeValue("show").equalsIgnoreCase("false");
                }
                result.add(new WoolObjective(team, name, id, color, place, craftable, show));
            }
            for (Element child : element.getChildren("wools")) {
                for (Element subChild : child.getChildren("wool")) {
                    TeamModule team;
                    try {
                        team = TeamUtils.getTeamById((child.getAttributeValue("team")));
                    } catch (NullPointerException e) {
                        try {
                            team = TeamUtils.getTeamById((subChild.getAttributeValue("team")));
                        } catch (NullPointerException ex) {
                            team = TeamUtils.getTeamById((element.getAttributeValue("team")));
                        }
                    }
                    DyeColor color = StringUtils.convertStringToDyeColor(subChild.getAttributeValue("color"));
                    BlockRegion place = RegionModuleBuilder.getRegion(subChild.getChildren().get(0)).getCenterBlock();
                    String name = color == null ? "Wool" : color.name() + " Wool";
                    if (element.getAttributeValue("name") != null) {
                        name = subChild.getAttributeValue("name");
                    }
                    String id = subChild.getAttributeValue("id");
                    if (id == null) {
                        id = subChild.getAttributeValue("color");
                    }
                    boolean craftable = true;
                    if (subChild.getAttributeValue("craftable") != null) {
                        craftable = !subChild.getAttributeValue("craftable").equalsIgnoreCase("false");
                    } else if (child.getAttributeValue("craftable") != null) {
                        craftable = !child.getAttributeValue("craftable").equalsIgnoreCase("false");
                    } else if (element.getAttributeValue("craftable") != null) {
                        craftable = !element.getAttributeValue("craftable").equalsIgnoreCase("false");
                    }
                    boolean show = true;
                    if (subChild.getAttributeValue("show") != null) {
                        show = !subChild.getAttributeValue("show").equalsIgnoreCase("false");
                    } else if (child.getAttributeValue("show") != null) {
                        show = !child.getAttributeValue("show").equalsIgnoreCase("false");
                    } else if (element.getAttributeValue("show") != null) {
                        show = !element.getAttributeValue("show").equalsIgnoreCase("false");
                    }
                    result.add(new WoolObjective(team, name, id, color, place, craftable, show));
                }
            }

        }
        return result;
    }

}
