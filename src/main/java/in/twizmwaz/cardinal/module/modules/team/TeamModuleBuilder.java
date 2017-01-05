package in.twizmwaz.cardinal.module.modules.team;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import org.apache.logging.log4j.core.helpers.Integers;
import org.bukkit.ChatColor;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.List;

@LoadTime(ModuleLoadTime.EARLIEST)
public class TeamModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TeamModule> load(Match match) {
        ModuleCollection<TeamModule> results = new ModuleCollection<>();
        Document doc = match.getDocument();
        Element teams = doc.getRootElement().getChild("teams");
        List<Element> teamElements = teams.getChildren();
        for (Element teamNode : teamElements) {
            String name = teamNode.getText();
            String id = teamNode.getAttributeValue("id") == null ? name.toLowerCase() : teamNode.getAttributeValue("id");
            int min;
            try {
                min = Integers.parseInt(teamNode.getAttribute("min").getValue(), doc.getRootElement().getChildren("blitz").size() != 0 ? 1 : 0);
            } catch (NullPointerException ex) {
                min = doc.getRootElement().getChildren("blitz").size() != 0 ? 1 : 0;
            }
            int max = Integers.parseInt(teamNode.getAttribute("max").getValue());
            int maxOverfill;
            try {
                maxOverfill = Integers.parseInt(teamNode.getAttribute("max-overfill").getValue(), (int) (1.25 * max));
            } catch (NullPointerException ex) {
                maxOverfill = (int) (1.25 * max);
            }
            int respawnLimit;
            try {
                respawnLimit = Integers.parseInt(teamNode.getAttribute("respawn-limit").getValue(), -1);
            } catch (NullPointerException ex) {
                respawnLimit = -1;
            }
            boolean plural = false;
            if (teamNode.getAttributeValue("plural") != null)
                plural = Numbers.parseBoolean(teamNode.getAttributeValue("plural"));
            ChatColor color = Parser.parseChatColor(teamNode.getAttribute("color").getValue());
            results.add(new TeamModule(match, name, id, min, max, maxOverfill, respawnLimit, color, plural, false));
        }
        results.add(new TeamModule(match, "Observers", "observers", 0, Integer.MAX_VALUE, Integer.MAX_VALUE, -1, ChatColor.AQUA, true, true));
        return results;
    }
}
