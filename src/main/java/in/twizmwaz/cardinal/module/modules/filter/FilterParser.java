package in.twizmwaz.cardinal.module.modules.filter;

import org.jdom2.Element;

public class FilterParser {
    
    private final String name;
    private FilterModule parent;
    
    public FilterParser(final Element element) {
        this.name = element.getParentElement().getAttributeValue("name");
        parent = null;
        if (element.getAttributeValue("parents") != null) {
            parent = FilterModuleBuilder.getFilter(element.getAttributeValue("parents"));
        } else if (element.getParentElement() != null && element.getParentElement().getAttributeValue("parents") != null) {
            parent = FilterModuleBuilder.getFilter(element.getParentElement().getAttributeValue("parents"));
        }
    }

    public String getName() {
        return name;
    }

    public FilterModule getParent() {
        return parent;
    }
}
