package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.ArmorKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.AttributeModifierKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.ClearKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.DoubleJumpKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.FlyKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.GameModeKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.HealthKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.ItemKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.KitArmor;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.KitItem;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.KnockbackReductionKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.PotionKit;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.WalkSpeedKit;
import in.twizmwaz.cardinal.util.ArmorType;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Strings;
import org.bukkit.GameMode;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@LoadTime(ModuleLoadTime.EARLIER)
public class KitBuilder implements ModuleBuilder {

    public static KitNode getKit(Element element, Document document, boolean proceed) {
        if (element.getName().equalsIgnoreCase("kit") || proceed) {
            List<Kit> kits = new ArrayList<>();
            String name = null;
            if (element.getAttributeValue("name") != null) name = element.getAttributeValue("name");
            if (element.getAttributeValue("id") != null) name = element.getAttributeValue("id");
            for (KitNode kit : GameHandler.getGameHandler().getMatch().getModules().getModules(KitNode.class)) {
                if (kit.getName().equalsIgnoreCase(name)) {
                    return kit;
                }
            }

            Boolean clear = element.getChildren("clear").size() > 0;
            Boolean clearItems = element.getChildren("clear-items").size() > 0;
            if (clear || clearItems) kits.add(new ClearKit(clear, clearItems));

            List<KitItem> items = new ArrayList<>();
            for (Element item : element.getChildren("item")) {
                items.add(Parser.getKitItem(item));
            }
            if (!items.isEmpty()) kits.add(new ItemKit(items));

            List<KitArmor> armor = new ArrayList<>(4);
            List<Element> armors = new ArrayList<>();
            armors.addAll(element.getChildren("helmet"));
            armors.addAll(element.getChildren("chestplate"));
            armors.addAll(element.getChildren("leggings"));
            armors.addAll(element.getChildren("boots"));
            for (Element piece : armors) {
                ItemStack itemStack = Parser.getItem(piece);
                ArmorType type = ArmorType.getArmorType(piece.getName());
                Boolean locked = Numbers.parseBoolean(element.getAttributeValue("locked"), false);
                armor.add(new KitArmor(itemStack, type, locked));
            }
            if (!armor.isEmpty()) kits.add(new ArmorKit(armor));

            if (element.getChildText("game-mode") != null) {
                GameMode gameMode;
                switch (element.getChildText("game-mode").toLowerCase()) {
                    case("creative"):
                        gameMode = GameMode.CREATIVE;
                        break;
                    case("survival"):
                        gameMode = GameMode.SURVIVAL;
                        break;
                    case("spectator"):
                        gameMode = GameMode.SPECTATOR;
                        break;
                    case("adventure"):
                        gameMode = GameMode.ADVENTURE;
                        break;
                    default:
                        gameMode = null;
                        break;
                }
                if (gameMode != null) kits.add(new GameModeKit(gameMode));
            }

            int health = element.getChildText("health") == null ? -1 : Numbers.parseInt(element.getChildText("health"));
            int foodLevel = element.getChildText("foodlevel") == null ? -1 : Numbers.parseInt(element.getChildText("foodlevel"));
            float saturation = element.getChildText("saturation") == null ? 0 : Float.parseFloat(element.getChildText("saturation"));
            if (health != -1 || foodLevel != -1 || saturation != 0) {
                kits.add(new HealthKit(health, foodLevel, saturation));
            }

            List<PotionEffect> potions = new ArrayList<>();
            for (Element potion : element.getChildren("potion")) {
                potions.add(Parser.getPotion(potion));
            }
            for (Element potion : element.getChildren("effect")) {
                potions.add(Parser.getPotion(potion));
            }
            if (!potions.isEmpty()) kits.add(new PotionKit(potions));

            List<AttributeModifier> attributes = new ArrayList<>();
            for (Element attr : element.getChildren("attribute")) {
                attributes.add(new AttributeModifier(UUID.randomUUID(), attr.getText(), Double.parseDouble(attr.getAttributeValue("amount", "0.0")), Parser.getOperation(attr.getAttributeValue("operation","add"))));
            }
            if (!attributes.isEmpty()) kits.add(new AttributeModifierKit(attributes));


            if (element.getChildText("walk-speed") != null) kits.add(new WalkSpeedKit(Float.parseFloat(element.getChildText("walk-speed")) / 5));
            if (element.getChildText("knockback-reduction") != null) kits.add(new KnockbackReductionKit(Float.parseFloat(element.getChildText("knockback-reduction"))));

            for (Element jump : element.getChildren("double-jump")) {
                boolean enabled = Numbers.parseBoolean(jump.getAttributeValue("enabled"), true);
                double power = Numbers.parseDouble(jump.getAttributeValue("power"), 3);
                double rechargeTime = Strings.timeStringToExactSeconds(jump.getAttributeValue("recharge-time", "2.5s"));
                boolean rechargeBeforeLanding = Numbers.parseBoolean(jump.getAttributeValue("recharge-before-landing"), false);
                kits.add(new DoubleJumpKit(enabled, power, rechargeTime, rechargeBeforeLanding));
            }

            for (Element jump : element.getChildren("fly")) {
                boolean canFly = Numbers.parseBoolean(jump.getAttributeValue("can-fly"), true);
                Boolean flying = jump.getAttributeValue("flying") != null ? Numbers.parseBoolean(jump.getAttributeValue("flying"), false) : null;
                float flySpeed = Float.parseFloat(jump.getAttributeValue("fly-speed", "1")) / 10F;
                kits.add(new FlyKit(canFly, flying, flySpeed));
            }

            String filter = element.getAttributeValue("filter", "always");
            String parent = element.getAttributeValue("parents", "");
            boolean force = Numbers.parseBoolean(element.getAttributeValue("force"), false);
            boolean potionParticles = Numbers.parseBoolean(element.getAttributeValue("potion-particles"), false);
            boolean discardPotionBottles = Numbers.parseBoolean(element.getAttributeValue("discard-potion-bottles"), true);
            boolean resetPearls = Numbers.parseBoolean(element.getAttributeValue("reset-ender-pearls"), false);
            return new KitNode(name, filter, force, potionParticles, discardPotionBottles, resetPearls, kits, parent);
        } else {
            return getKit(element.getParentElement(), document, true);
        }
    }

    public static KitNode getKit(Element element) {
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
        List<Kit> subKits = new ArrayList<>();
        for (Module m : results) {
            for (Kit kit : ((KitNode) m).getKits()) subKits.add(kit);
        }
        results.addAll(subKits);
        results.add(new KitApplier());
        return results;
    }

}
