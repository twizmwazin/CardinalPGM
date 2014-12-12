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
            PgmTeam team = match.getTeamById(element.getAttributeValue("team"));
            for (Element subElement : element.getChildren()) {
                DyeColor color = StringUtils.convertStringToDyeColor(subElement.getAttributeValue("color"));
                BlockRegion place = new BlockRegion(new BlockParser(subElement.getChild("block")));
                result.add(new WoolObjective(team, color, place));
            }
        }

        return result;
    }

}
