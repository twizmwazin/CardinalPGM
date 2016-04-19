package in.twizmwaz.cardinal.module.modules.proximity;

import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class ProximityInfo {

    public Set<Vector> locations;
    public Boolean horizontal;
    public Boolean needsTouch;
    public GameObjectiveProximityHandler.ProximityMetric metric;

    public ProximityInfo(Vector location, boolean horizontal, boolean needsTouch, GameObjectiveProximityHandler.ProximityMetric metric) {
        this.horizontal = horizontal;
        this.needsTouch = needsTouch;
        this.metric = metric;
        this.locations = new HashSet<>();
        if (location != null) locations.add(location);
        if (horizontal) {
            for (Vector vec : this.locations) {
                vec.setY(0);
            }
        }
    }

    public void setLocations(Set<Vector> locations) {
        this.locations = locations;
        if (horizontal) {
            for (Vector vec : this.locations) {
                vec.setY(0);
            }
        }
    }

}
