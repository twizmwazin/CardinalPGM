package in.twizmwaz.cardinal.module.modules.itemMods;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ItemMatch {

    private final Set<Material> singleMaterials;
    private final Set<Pair<Material, Integer>> materials;
    private final boolean allMaterials;
    private final boolean allBlocks;

    protected ItemMatch(Set<Material> singleMaterials, Set<Pair<Material, Integer>> materials, boolean allMaterials, boolean allBlocks) {
        this.singleMaterials = singleMaterials;
        this.materials = materials;
        this.allMaterials = allMaterials;
        this.allBlocks = allBlocks;
    }

    protected boolean match(ItemStack item) {
        return allMaterials || (allBlocks && item.getType().isBlock()) || singleMaterials.contains(item.getType()) ||
                materials.contains(new ImmutablePair<>(item.getType(), (int)item.getData().getData()));
    }

}
