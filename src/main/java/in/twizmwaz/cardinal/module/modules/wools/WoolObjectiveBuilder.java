package in.twizmwaz.cardinal.module.modules.wools;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.DyeColor;
import org.bukkit.util.Vector;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class WoolObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<WoolObjective> load(Match match) {
        ModuleCollection<WoolObjective> result = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("wools")) {
            for (Element subElement : element.getChildren("wool")) {
                TeamModule team;
                try {
                    team = Teams.getTeamById(element.getAttributeValue("team")).orNull();
                } catch (NullPointerException e) {
                    team = Teams.getTeamById((subElement.getAttributeValue("team"))).orNull();
                }
                DyeColor color = Parser.parseDyeColor(subElement.getAttributeValue("color"));
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
                Vector location = null;
                if (subElement.getAttributeValue("location") != null) {
                    location = new Vector(Numbers.parseDouble(subElement.getAttributeValue("location").replaceAll(" ", ",").split(",")[0]), Numbers.parseDouble(subElement.getAttributeValue("location").replaceAll(" ", ",").split(",")[1]), Numbers.parseDouble(subElement.getAttributeValue("location").replaceAll(" ", ",").split(",")[2]));
                } else if (element.getAttributeValue("location") != null) {
                    location = new Vector(Numbers.parseDouble(element.getAttributeValue("location").replaceAll(" ", ",").split(",")[0]), Numbers.parseDouble(element.getAttributeValue("location").replaceAll(" ", ",").split(",")[1]), Numbers.parseDouble(element.getAttributeValue("location").replaceAll(" ", ",").split(",")[2]));
                }
                result.add(new WoolObjective(team, name, id, color, place, craftable, show, location));
            }
            for (Element child : element.getChildren("wools")) {
                for (Element subChild : child.getChildren("wool")) {
                    TeamModule team;
                    try {
                        team = Teams.getTeamById((child.getAttributeValue("team"))).orNull();
                    } catch (NullPointerException e) {
                        try {
                            team = Teams.getTeamById((subChild.getAttributeValue("team"))).orNull();
                        } catch (NullPointerException ex) {
                            team = Teams.getTeamById((element.getAttributeValue("team"))).orNull();
                        }
                    }
                    DyeColor color = Parser.parseDyeColor(subChild.getAttributeValue("color"));
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
                    Vector location = null;
                    if (subChild.getAttributeValue("location") != null) {
                        location = new Vector(Numbers.parseDouble(subChild.getAttributeValue("location").replaceAll(" ", ",").split(",")[0]), Numbers.parseDouble(subChild.getAttributeValue("location").replaceAll(" ", ",").split(",")[1]), Numbers.parseDouble(subChild.getAttributeValue("location").replaceAll(" ", ",").split(",")[2]));
                    } else if (child.getAttributeValue("location") != null) {
                        location = new Vector(Numbers.parseDouble(child.getAttributeValue("location").replaceAll(" ", ",").split(",")[0]), Numbers.parseDouble(child.getAttributeValue("location").replaceAll(" ", ",").split(",")[1]), Numbers.parseDouble(child.getAttributeValue("location").replaceAll(" ", ",").split(",")[2]));
                    } else if (element.getAttributeValue("location") != null) {
                        location = new Vector(Numbers.parseDouble(element.getAttributeValue("location").replaceAll(" ", ",").split(",")[0]), Numbers.parseDouble(element.getAttributeValue("location").replaceAll(" ", ",").split(",")[1]), Numbers.parseDouble(element.getAttributeValue("location").replaceAll(" ", ",").split(",")[2]));
                    }
                    result.add(new WoolObjective(team, name, id, color, place, craftable, show, location));
                }
            }

        }
        return result;
    }

}
