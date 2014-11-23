package in.twizmwaz.cardinal.module.modules.kit.kit;

import in.twizmwaz.cardinal.module.modules.kit.kit.contents.KitArmor;
import in.twizmwaz.cardinal.module.modules.kit.kit.contents.KitItem;
import org.bukkit.potion.PotionEffect;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kevin on 11/21/14.
 */
public class Kit {

    private String name;
    private boolean force;
    private List<Kit> parents;
    private List<KitItem> items;
    private List<KitArmor> armor;
    private List<PotionEffect> portions;

    public Kit(Element element) {

    }

    private String parseName(Element element) {
        return element.getAttribute(name).getValue();
    }

    private boolean parseForce(Element element) {
        try {
            String s = element.getAttribute("force").getValue();
            return Boolean.valueOf(s);
        } catch (NullPointerException ex) {
            return false;
        }
    }

    private List<Kit> parseParents(Element element) {
        List<Kit> result = new ArrayList<Kit>();
        List<String> parName = Arrays.asList(element.getAttributeValue("parents").replaceAll(" ", "").split(","));
        for (Element sibling : element.getParentElement().getChildren()) {
            if (parName.contains(sibling.getAttributeValue("name"))) {
                result.add(new Kit(sibling));
            }
        }
        return result;

    }

    private List<Element> parseItems(Element element) {
        return element.getChildren("item");
    }



}
