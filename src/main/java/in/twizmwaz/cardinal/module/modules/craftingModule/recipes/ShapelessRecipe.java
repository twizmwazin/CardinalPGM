package in.twizmwaz.cardinal.module.modules.craftingModule.recipes;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Set;

public class ShapelessRecipe extends AbstractRecipe {

    public ShapelessRecipe(ItemStack result, Set<Pair<Integer, MaterialData>> ingredients) {
        org.bukkit.inventory.ShapelessRecipe recipe = new org.bukkit.inventory.ShapelessRecipe(result);
        for (Pair<Integer, MaterialData> ingredient : ingredients) {
            recipe.addIngredient(ingredient.getKey(), ingredient.getValue());
        }
        setRecipe(recipe);
    }

}
