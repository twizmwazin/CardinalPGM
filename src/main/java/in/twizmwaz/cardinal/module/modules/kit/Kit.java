package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 11/28/14.
 */

@ModuleInfo(name = "kit", builder = KitBuilder.class, multiple = false)
public class Kit extends Module {

    private List<KitInventory> kits;

    public Kit(KitBuilder builder) {
        this.kits = new ArrayList<>();
    }


}
