package in.twizmwaz.cardinal.module.modules.spawn;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.regions.Region;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.util.Vector;

import java.util.List;

public class SpawnModule implements Module {
    
    private final TeamModule team;
    private final List<Pair<Region, Vector>> region;
    private final Kit kit;
    private final boolean safe;
    private final boolean sequential;
    //private final Filter filter;

    public SpawnModule(TeamModule team, List<Pair<Region, Vector>> region, Kit kit, boolean safe, boolean sequential) {
        this.team = team;
        this.region = region;
        this.kit = kit;
        this.safe = safe;
        this.sequential = sequential;
    }
    
    @Override
    public void unload() {
    }

    public TeamModule getTeam() {
        return team;
    }

    public List<Pair<Region, Vector>> getRegion() {
        return region;
    }

    public Kit getKit() {
        return kit;
    }

    public boolean isSafe() {
        return safe;
    }

    public boolean isSequential() {
        return sequential;
    }
}
