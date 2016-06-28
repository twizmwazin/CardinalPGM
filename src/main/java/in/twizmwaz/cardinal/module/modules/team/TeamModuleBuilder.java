package in.twizmwaz.cardinal.module.modules.team;

import in.twizmwaz.cardinal.Cardinal;
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

@LoadTime(ModuleLoadTime.EARLIEST)
public class TeamModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TeamModule> load(Match match) {
        ModuleCollection<TeamModule> results = new ModuleCollection<>();
        Document doc = match.getDocument();
        Element teams = doc.getRootElement().getChild("teams");
        Element players = doc.getRootElement().getChild("players");
        if (teams != null) {
            for (Element teamNode : teams.getChildren()) {
                String name = teamNode.getText();
                String id = teamNode.getAttributeValue("id") == null ? name.toLowerCase() : teamNode.getAttributeValue("id");
                int min = Numbers.parseInt(Parser.getOrderedAttribute("min", teamNode, teams), doc.getRootElement().getChild("blitz") != null ? 1 : 0);
                int max = Integers.parseInt(Parser.getOrderedAttribute("max", teamNode, teams));
                int maxOverfill = Integers.parseInt(Parser.getOrderedAttribute("max-overfill"), (int) (1.25 * max));
                boolean plural = Numbers.parseBoolean(Parser.getOrderedAttribute("plural", teamNode, teams), false);
                ChatColor color = Parser.parseChatColor(Parser.getOrderedAttribute("color", teamNode, teams));
                results.add(new TeamModule(match, name, id, min, max, maxOverfill, color, plural, false));
            }
        }
        if (players != null) {
            if (results.size() == 0) {
                int min = Numbers.parseInt(Parser.getOrderedAttribute("min", players), 0);
                int max = Numbers.parseInt(Parser.getOrderedAttribute("max", players));
                int maxOverfill = Numbers.parseInt(Parser.getOrderedAttribute("max-overfill", players), (int) (1.25 * max));
                boolean colors = Numbers.parseBoolean(Parser.getOrderedAttribute("colors", players), false);
                results.add(new PlayerModuleManager(match, min, max, maxOverfill, colors));
            } else {
                Cardinal.getInstance().getLogger().warning("Teams and players modules can't be combined, only teams will be loaded");
            }
        }
        results.add(new TeamModule(match, "Observers", "observers", 0, Integer.MAX_VALUE, Integer.MAX_VALUE, ChatColor.AQUA, true, true));
        return results;
    }

}
