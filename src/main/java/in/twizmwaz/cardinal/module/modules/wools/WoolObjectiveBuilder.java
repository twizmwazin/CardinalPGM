package in.twizmwaz.cardinal.module.modules.wools;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.regions.parsers.BlockParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.DyeColor;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class WoolObjectiveBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> result = new ArrayList<>();
        for (Element element : match.getDocument().getRootElement().getChildren("wools")) {
            for (Element subElement : element.getChildren("wool")) {
                PgmTeam team;
                try {
                    team = match.getTeamById(element.getAttributeValue("team"));
                } catch (NullPointerException e) {
                    team = match.getTeamById(subElement.getAttributeValue("team"));
                }
                DyeColor color = StringUtils.convertStringToDyeColor(subElement.getAttributeValue("color"));
                BlockRegion place = new BlockRegion(new BlockParser(subElement.getChild("block")));
                result.add(new WoolObjective(team, color, place));
            }
            for (Element child : element.getChildren("wools")) {
                for (Element subChild : child.getChildren("wool")) {
                    PgmTeam team;
                    try {
                        team = match.getTeamById(child.getAttributeValue("team"));
                    } catch (NullPointerException e) {
                        try {
                            team = match.getTeamById(subChild.getAttributeValue("team"));
                        } catch (NullPointerException ex) {
                            team = match.getTeamById(element.getAttributeValue("team"));
                        }
                    }
                    DyeColor color = StringUtils.convertStringToDyeColor(subChild.getAttributeValue("color"));
                    BlockRegion place = new BlockRegion(new BlockParser(subChild.getChild("block")));
                }
            }

        }

        return result;
    }

}
