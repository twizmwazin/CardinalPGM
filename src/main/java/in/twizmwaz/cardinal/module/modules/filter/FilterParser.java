package in.twizmwaz.cardinal.module.modules.filter;

import org.jdom2.Element;

public class FilterParser {

    private final String name;
    private FilterModule parent;

    public FilterParser(final Element element) {
        this.name =
                element.getAttributeValue("name") != null ? element.getAttributeValue("name") :
                        element.getAttributeValue("id") != null ? element.getAttributeValue("id") :
                                element.getParentElement() != null && element.getParentElement().getAttributeValue("name") != null ? element.getParentElement().getAttributeValue("name") :
                                        element.getParentElement() != null && element.getParentElement().getAttributeValue("id") != null ? element.getParentElement().getAttributeValue("id") : null;
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
