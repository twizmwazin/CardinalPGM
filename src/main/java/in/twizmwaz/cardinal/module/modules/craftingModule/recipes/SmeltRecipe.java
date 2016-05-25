package in.twizmwaz.cardinal.module.modules.craftingModule.recipes;

import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class SmeltRecipe extends AbstractRecipe {

    public SmeltRecipe(ItemStack result, MaterialData source) {
        setRecipe(new FurnaceRecipe(result, source));
    }

}
