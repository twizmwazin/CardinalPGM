package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.bukkit.Material;
import org.jdom2.Element;

public class ClassFilterParser extends FilterParser {

    private String classModule;

    public ClassFilterParser(final Element element) {
        super(element);
        classModule = element.getText();
    }

    public String getClassModule() {
        return classModule;
    }
}
