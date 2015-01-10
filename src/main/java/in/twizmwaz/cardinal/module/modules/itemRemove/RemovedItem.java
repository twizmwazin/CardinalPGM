package in.twizmwaz.cardinal.module.modules.itemRemove;

import org.bukkit.Material;

public class RemovedItem {
    
    private final Material material;
    private final short data;

    public RemovedItem(Material material, short data) {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return material;
    }

    public int getData() {
        return data;
    }
}
