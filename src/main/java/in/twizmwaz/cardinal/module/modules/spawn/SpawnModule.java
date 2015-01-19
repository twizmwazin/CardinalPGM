package in.twizmwaz.cardinal.module.modules.spawn;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class SpawnModule implements Module {

    private final TeamModule team;
    private final List<Pair<RegionModule, Vector>> regions;
    private final String kit;
    private final boolean safe;
    private final boolean sequential;
    //private final Filter filter;
    private int position;

    public SpawnModule(TeamModule team, List<Pair<RegionModule, Vector>> regions, String kit, boolean safe, boolean sequential) {
        this.team = team;
        this.regions = regions;
        this.kit = kit;
        this.safe = safe;
        this.sequential = sequential;
        this.position = 0;
    }

    @Override
    public void unload() {
    }

    public TeamModule getTeam() {
        return team;
    }

    public List<Pair<RegionModule, Vector>> getRegions() {
        return regions;
    }

    public String getKit() {
        return kit;
    }

    public boolean isSafe() {
        return safe;
    }

    public boolean isSequential() {
        return sequential;
    }

    public Location getLocation() {
        if (sequential) {
            Location location = regions.get(position).getLeft().getRandomPoint().getLocation();
            location.setDirection(regions.get(position).getRight());
            position++;
            if (position == regions.size()) position = 0;
            return location;
        } else {
            Random random = new Random();
            int use = random.nextInt(regions.size());
            Location location = regions.get(use).getLeft().getRandomPoint().getLocation();
            location.setDirection(regions.get(use).getRight());
            return location;
        }
    }
}
