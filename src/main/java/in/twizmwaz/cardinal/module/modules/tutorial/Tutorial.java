package in.twizmwaz.cardinal.module.modules.tutorial;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.bukkit.event.HandlerList;

import java.util.List;

public class Tutorial implements Module {

    private final String title;
    private final List<String> line;
    private final RegionModule teleport;

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    protected Tutorial(final String title, final List<String> line, final RegionModule teleport) {
        this.title = title;
        this.line = line;
        this.teleport = teleport;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLine() {
        return line;
    }

    public String getLine(int index) {
        return line.get(index);
    }

    public RegionModule getTeleport() {
        return teleport;
    }

}
