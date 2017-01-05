package in.twizmwaz.cardinal.module.modules.classModule;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import in.twizmwaz.cardinal.module.modules.kit.KitNode;
import org.bukkit.Material;
import org.jdom2.Element;

@LoadTime(ModuleLoadTime.EARLIER)
public class ClassModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ClassModule> load(Match match) {
        ModuleCollection<ClassModule> results = new ModuleCollection<>();
        for (Element classes : match.getDocument().getRootElement().getChildren("classes")) {
            for (Element classElement : classes.getChildren("class")) {
                String name = null;
                if (classElement.getAttributeValue("name") != null) {
                    name = classElement.getAttributeValue("name");
                } else if (classes.getAttributeValue("name") != null) {
                    name = classes.getAttributeValue("name");
                }
                String description = null;
                if (classElement.getAttributeValue("description") != null) {
                    description = classElement.getAttributeValue("description");
                } else if (classes.getAttributeValue("description") != null) {
                    description = classes.getAttributeValue("description");
                }
                String longDescription = description;
                if (classElement.getAttributeValue("longdescription") != null) {
                    longDescription = classElement.getAttributeValue("longdescription");
                } else if (classes.getAttributeValue("longdescription") != null) {
                    longDescription = classes.getAttributeValue("longdescription");
                }
                Material icon = Material.STONE;
                if (classElement.getAttributeValue("icon") != null) {
                    icon = Material.matchMaterial(classElement.getAttributeValue("icon"));
                } else if (classes.getAttributeValue("icon") != null) {
                    icon = Material.matchMaterial(classes.getAttributeValue("icon"));
                }
                boolean sticky = false;
                if (classElement.getAttributeValue("sticky") != null) {
                    sticky = classElement.getAttributeValue("sticky").equalsIgnoreCase("true");
                } else if (classes.getAttributeValue("sticky") != null) {
                    sticky = classes.getAttributeValue("sticky").equalsIgnoreCase("true");
                }
                boolean defaultClass = false;
                if (classElement.getAttributeValue("default") != null) {
                    defaultClass = classElement.getAttributeValue("default").equalsIgnoreCase("true");
                } else if (classes.getAttributeValue("default") != null) {
                    defaultClass = classes.getAttributeValue("default").equalsIgnoreCase("true");
                }
                boolean restrict = false;
                if (classElement.getAttributeValue("restrict") != null) {
                    restrict = !classElement.getAttributeValue("restrict").equalsIgnoreCase("false");
                } else if (classes.getAttributeValue("restrict") != null) {
                    restrict = !classes.getAttributeValue("restrict").equalsIgnoreCase("false");
                }
                KitNode kit = classElement.getChildren().size() > 0 ? KitBuilder.getKit(classElement.getChildren().get(0)) : null;
                results.add(new ClassModule(name, description, longDescription, icon, sticky, defaultClass, restrict, kit));
            }
        }
        return results;
    }

}
