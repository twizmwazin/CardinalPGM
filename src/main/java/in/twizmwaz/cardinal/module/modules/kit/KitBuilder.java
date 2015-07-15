package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.*;
import in.twizmwaz.cardinal.util.ArmorType;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class KitBuilder implements ModuleBuilder {

    public static Kit getKit(Element element, Document document, boolean proceed) {
        if (element.getName().equalsIgnoreCase("kit") || proceed) {
            String name = null;
            if (element.getAttributeValue("name") != null) {
                name = element.getAttributeValue("name");
                for (Kit kit : GameHandler.getGameHandler().getMatch().getModules().getModules(Kit.class)) {
                    if (kit.getName().equalsIgnoreCase(name)) {
                        return kit;
                    }
                }
            }
            List<KitItem> items = new ArrayList<>(36);
            for (Element item : element.getChildren("item")) {
                ItemStack itemStack = Parser.getItem(item);
                int slot = item.getAttributeValue("slot") != null ? Numbers.parseInt(item.getAttributeValue("slot")) : -1;
                items.add(new KitItem(itemStack, slot));
            }
            List<KitArmor> armor = new ArrayList<>(4);
            List<Element> armors = new ArrayList<>();
            armors.addAll(element.getChildren("helmet"));
            armors.addAll(element.getChildren("chestplate"));
            armors.addAll(element.getChildren("leggings"));
            armors.addAll(element.getChildren("boots"));
            for (Element piece : armors) {
                ItemStack itemStack = new ItemStack(Material.matchMaterial(piece.getText()), 1);
                if (piece.getAttributeValue("damage") != null) {
                    itemStack.setDurability(Short.parseShort(piece.getAttributeValue("damage")));
                }
                if (itemStack.getItemMeta() instanceof LeatherArmorMeta && piece.getAttributeValue("color") != null) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
                    meta.setColor(MiscUtil.convertHexToRGB(piece.getAttributeValue("color")));
                    itemStack.setItemMeta(meta);
                }
                try {
                    for (String raw : piece.getAttributeValue("enchantment").split(";")) {
                        String[] enchant = raw.split(":");
                        try {
                            itemStack.addUnsafeEnchantment(Enchantment.getByName(Strings.getTechnicalName(enchant[0])), Numbers.parseInt(enchant[1]));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            itemStack.addUnsafeEnchantment(Enchantment.getByName(Strings.getTechnicalName(enchant[0])), 1);
                        }
                    }
                } catch (NullPointerException e) {

                }
                ArmorType type = ArmorType.getArmorType(piece.getName());
                armor.add(new KitArmor(itemStack, type));
            }
            List<PotionEffect> potions = new ArrayList<>();
            for (Element potion : element.getChildren("potion")) {
                potions.add(Parser.getPotion(potion));
            }
            List<KitBook> books = new ArrayList<>();
            for (Element book : element.getChildren("book")) {
                String title = null;
                if (book.getChildText("title") != null) {
                    title = book.getChildText("title");
                }
                String author = null;
                if (book.getChildText("author") != null) {
                    author = book.getChildText("author");
                }
                int slot = book.getAttributeValue("slot") != null ? Numbers.parseInt(book.getAttributeValue("slot")) : -1;
                List<String> pages = new ArrayList<>();
                for (Element page : book.getChild("pages").getChildren("page")) {
                    pages.add(ChatColor.translateAlternateColorCodes('`', page.getText()).replace("\u0009", ""));
                }
                books.add(new KitBook(title, author, pages, slot));
            }
            String parent = element.getAttributeValue("parents");
            boolean force = element.getAttributeValue("force") != null && Boolean.parseBoolean(element.getAttributeValue("force"));
            boolean potionParticles = element.getAttributeValue("potion-particles") != null && Numbers.parseBoolean(element.getAttributeValue("potion-particles"));
            boolean resetPearls = element.getAttributeValue("reset-ender-pearls") != null && Numbers.parseBoolean(element.getAttributeValue("reset-ender-pearls"));
            boolean clear = element.getChildren("clear").size() > 0;
            boolean clearItems = element.getChildren("clear-items").size() > 0;
            int health = element.getChildText("health") == null ? -1 : Numbers.parseInt(element.getChild("health").getText()) / 2;
            float saturation = element.getChildText("saturation") == null ? 0 : Float.parseFloat(element.getChildText("saturation"));
            int foodLevel = element.getChildText("foodlevel") == null ? -1 : Numbers.parseInt(element.getChildText("foodlevel"));
            float walkSpeed = element.getChildText("walk-speed") == null ? 0.2F : Float.parseFloat(element.getChildText("walk-speed")) / 5;
            float knockback = element.getChildText("knockback-reduction") == null ? 0F : Float.parseFloat(element.getChildText("knockback-reduction"));
            boolean jump = false;
            if (element.getChildren("double-jump").size() > 0) jump = true;
            float flySpeed = element.getChildText("fly-speed") == null ? 0.2F : Float.parseFloat(element.getChildText("fly-speed")) / 5;
            return new Kit(name, items, armor, potions, books, parent, force, potionParticles, resetPearls, clear, clearItems, health, saturation, foodLevel, walkSpeed, knockback, jump, flySpeed);
        } else {
            return getKit(element.getParentElement(), document, true);
        }
    }

    public static Kit getKit(Element element) {
        return getKit(element, GameHandler.getGameHandler().getMatch().getDocument(), false);
    }

    @Override
    public ModuleCollection<Module> load(Match match) {
        ModuleCollection<Module> results = new ModuleCollection<>();
        for (Element kits : match.getDocument().getRootElement().getChildren("kits")) {
            for (Element element : kits.getChildren("kit")) {
                results.add(getKit(element));
            }
        }
        results.add(new KitApplier());
        return results;
    }

}
