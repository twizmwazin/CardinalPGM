package in.twizmwaz.cardinal.module.modules.craftingModule.recipes;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Set;

public class ShapedRecipe extends AbstractRecipe {

    public ShapedRecipe(ItemStack result, String[] shape, Set<Pair<Character, MaterialData>> ingredients) {
        org.bukkit.inventory.ShapedRecipe recipe = new org.bukkit.inventory.ShapedRecipe(result);
        recipe.shape(shape);
        for (Pair<Character, MaterialData> ingredient : ingredients) {
            recipe.setIngredient(ingredient.getKey(), ingredient.getValue());
        }
        setRecipe(recipe);
    }

}
