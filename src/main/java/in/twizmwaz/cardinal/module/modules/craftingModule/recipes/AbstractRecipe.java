package in.twizmwaz.cardinal.module.modules.craftingModule.recipes;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Recipe;

public class AbstractRecipe {

    private Recipe recipe;
    private boolean override;
    private boolean overrideAll;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public boolean getOverride() {
        return this.override;
    }

    public void setOverrideAll(boolean overrideAll) {
        this.overrideAll = overrideAll;
    }

    public boolean getOverrideAll() {
        return this.overrideAll;
    }

    public void register() {
        Bukkit.getServer().addRecipe(recipe);
    }

}
