package in.twizmwaz.cardinal.module.modules.team;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.util.ParseUtils;
import org.apache.logging.log4j.core.helpers.Integers;
import org.bukkit.ChatColor;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.List;

@BuilderData(load = ModuleLoadTime.EARLIEST)
public class TeamModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<TeamModule> results = new ModuleCollection<>();
        Document doc = match.getDocument();
        Element teams = doc.getRootElement().getChild("teams");
        List<Element> teamElements = teams.getChildren();
        for (Element teamNode : teamElements) {
            String name = teamNode.getText();
            String id = teamNode.getAttributeValue("id") == null ? name.toLowerCase().split(" ")[0] : teamNode.getAttribute("id").getValue();
            int max = Integers.parseInt(teamNode.getAttribute("max").getValue());
            int maxOverfill;
            try {
                maxOverfill = Integers.parseInt(teamNode.getAttribute("max-overfill").getValue(), (int) (1.2 * max));
            } catch (NullPointerException ex) {
                maxOverfill = (int) (1.2 * max);
            }
            int respawnLimit;
            try {
                respawnLimit = Integers.parseInt(teamNode.getAttribute("respawn-limit").getValue(), -1);
            } catch (NullPointerException ex) {
                respawnLimit = -1;
            }
            ChatColor color = ParseUtils.parseChatColor(teamNode.getAttribute("color").getValue());
            results.add(new TeamModule(match, name, id, max, maxOverfill, respawnLimit, color, false, false));
        }
        results.add(new TeamModule(match, "Observers", "observers", Integer.MAX_VALUE, Integer.MAX_VALUE, -1, ChatColor.AQUA, true, true));
        return results;
    }
}
