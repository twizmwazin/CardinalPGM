package in.twizmwaz.cardinal.module.modules.craftingModule;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.craftingModule.recipes.AbstractRecipe;
import in.twizmwaz.cardinal.module.modules.craftingModule.recipes.ShapedRecipe;
import in.twizmwaz.cardinal.module.modules.craftingModule.recipes.ShapelessRecipe;
import in.twizmwaz.cardinal.module.modules.craftingModule.recipes.SmeltRecipe;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CraftingModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<CraftingModule> load(Match match) {
        Set<AbstractRecipe> recipes = new HashSet<>();
        Set<Material> disabledMaterials = new HashSet<>();
        Set<MaterialData> disabledMaterialData = new HashSet<>();
        for (Element crafting : match.getDocument().getRootElement().getChildren("crafting")) {
            for (Element shaped : crafting.getChildren("shaped"))
                recipes.add(addOverrides(getShapedRecipe(shaped), shaped));
            for (Element shapeless : crafting.getChildren("shapeless"))
                recipes.add(addOverrides(getShapelessRecipe(shapeless), shapeless));
            for (Element smelt : crafting.getChildren("smelt"))
                recipes.add(addOverrides(getSmeltRecipe(smelt), smelt));
            for (Element disable : crafting.getChildren("disable")) {
                MaterialData data = Parser.parseMaterialData(disable.getText());
                if ((data.getData() == -1))
                    disabledMaterials.add(data.getItemType());
                else
                    disabledMaterialData.add(data);
            }
        }
        for (AbstractRecipe recipe : recipes) {
            if (recipe.getOverride()) disabledMaterialData.add(recipe.getRecipe().getResult().getData());
            if (recipe.getOverrideAll()) disabledMaterials.add(recipe.getRecipe().getResult().getType());
        }
        return new ModuleCollection<>(new CraftingModule(recipes, disabledMaterials, disabledMaterialData));
    }

    private AbstractRecipe getShapedRecipe(Element element) {
        ItemStack result = Parser.getItem(element.getChild("result"));
        List<String> rows = new ArrayList<>();
        for (Element row : element.getChild("shape").getChildren("row")) {
            rows.add(row.getText());
        }
        Set<Pair<Character, MaterialData>> ingredients = new HashSet<>();
        for (Element ingredient : getIngredientChilds(element)) {
            ingredients.add(new ImmutablePair<>(ingredient.getAttributeValue("symbol").charAt(0), Parser.parseMaterialData(ingredient.getText())));
        }
        return new ShapedRecipe(result, rows.toArray(new String[rows.size()]), ingredients);
    }

    private ShapelessRecipe getShapelessRecipe(Element element) {
        ItemStack result = Parser.getItem(element.getChild("result"));
        Set<Pair<Integer, MaterialData>> ingredients = new HashSet<>();
        for (Element ingredient : getIngredientChilds(element)) {
            ingredients.add(new ImmutablePair<>(Numbers.parseInt(ingredient.getAttributeValue("amount"), 1), Parser.parseMaterialData(ingredient.getText())));
        }
        return new ShapelessRecipe(result, ingredients);
    }

    private SmeltRecipe getSmeltRecipe(Element element) {
        ItemStack result = Parser.getItem(element.getChild("result"));
        MaterialData source = Parser.parseMaterialData(getIngredientChilds(element).get(0).getText());
        return new SmeltRecipe(result, source);
    }

    private AbstractRecipe addOverrides(AbstractRecipe recipe, Element element) {
        recipe.setOverride(Numbers.parseBoolean(element.getAttributeValue("override"), false));
        recipe.setOverrideAll(Numbers.parseBoolean(element.getAttributeValue("override-all"), false));
        return recipe;
    }

    private List<Element> getIngredientChilds(Element element) {
        List<Element> result = new ArrayList<>();
        result.addAll(element.getChildren("ingredient"));
        result.addAll(element.getChildren("i"));
        return result;
    }

}
