package in.twizmwaz.cardinal.module.modules.tutorial;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.bukkit.ChatColor;

import java.util.List;

public class Stage {

    private String title;
    private List<String> lines;
    private RegionModule teleport;

    public Stage(String title, List<String> lines, RegionModule teleport) {
        this.title = title;
        this.lines = lines;
        this.teleport = teleport;
    }

    public String getTitle() {
        return title;
    }

    public String getFormattedTitle() {
        return "   " + ChatColor.YELLOW + title;
    }

    public List<String> getLines() {
        return lines;
    }

    public RegionModule getTeleport() {
        return teleport;
    }
}
