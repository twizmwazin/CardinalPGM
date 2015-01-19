package in.twizmwaz.cardinal.module.modules.itemKeep;

import org.bukkit.Material;

import java.util.Set;

public class KeptItem {

    private final Set<Material> materials;
    private final short data;

    public KeptItem(Set<Material> materials, short data) {
        this.materials = materials;
        this.data = data;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public short getData() {
        return data;
    }

}
