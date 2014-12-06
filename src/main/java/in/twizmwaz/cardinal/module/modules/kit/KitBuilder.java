package in.twizmwaz.cardinal.module.modules.kit;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.List;

/**
 * Created by kevin on 11/28/14.
 */
public class KitBuilder extends ModuleBuilder {

    Element kitElement;
    List<KitInventory> inventories;

    public KitBuilder(Document doc) {
        super(doc);
        this.kitElement = doc.getRootElement().getChild("kits");
        for (Element kit : kitElement.getChildren("kit")) {
            String name = kit.getAttributeValue("name");
            PlayerInventory inventory = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
            for (Element item : kit.getChildren("item")) {
                ItemStack itemStack = new ItemStack(StringUtils.convertStringToMaterial(item.getText()));
                if (item.getAttribute("damage") != null) {
                    itemStack.setDurability(Short.parseShort(item.getAttributeValue("damage")));
                }
                if (item.getAttribute("enchantment") != null) {
                    for (String enchant : item.getAttributeValue("enchantment").split(";")) {
                        String[] data = enchant.split(":");
                        itemStack.addEnchantment(StringUtils.convertStringToEnchantment(data[0]), Integer.parseInt(data[1]));
                    }
                }
                if (item.getAttribute("amount") != null) {
                    itemStack.setAmount(Integer.parseInt(item.getAttributeValue("amount")));
                }
                if (item.getAttribute("name") != null) {
                    itemStack.getItemMeta().setDisplayName(item.getAttributeValue("name"));
                }
                inventory.setItem(Integer.getInteger(item.getAttributeValue("slot")), itemStack);
            }
        }
        

    }



}
