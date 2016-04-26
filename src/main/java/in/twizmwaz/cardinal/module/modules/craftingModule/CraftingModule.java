package in.twizmwaz.cardinal.module.modules.craftingModule;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.craftingModule.recipes.AbstractRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.material.MaterialData;

import java.util.Iterator;
import java.util.Set;

public class CraftingModule implements Module {

    public CraftingModule(Set<AbstractRecipe> recipes, Set<Material> disabledMaterials, Set<MaterialData> disabledMaterialData) {
        Iterator<org.bukkit.inventory.Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()) {
            org.bukkit.inventory.Recipe recipe = recipeIterator.next();
            if (disabledMaterials.contains(recipe.getResult().getType()) || disabledMaterialData.contains(recipe.getResult().getData())) {
                Bukkit.getConsoleSender().sendMessage("Removing:" + recipe + " result:" + recipe.getResult());
                recipeIterator.remove();
            }
        }
        for (AbstractRecipe recipe : recipes) {
            recipe.register();
        }
    }

    @Override
    public void unload() {
        Bukkit.resetRecipes();
        HandlerList.unregisterAll(this);
    }

}
