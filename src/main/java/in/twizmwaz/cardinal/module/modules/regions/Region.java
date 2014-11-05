package in.twizmwaz.cardinal.module.modules.regions;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by kevin on 10/26/14.
 */
public abstract class Region extends Module {

    List<Region> regions;
    String name;

    public Region(String name) {
        this.name = name;
        regions.add(this);
    }

    public Region(Element e) {
        this.name = e.getAttribute("name");
        regions.add(this);
    }

    public String getName() {
        return name;
    }

    public abstract boolean contains(BlockRegion region);


}
