package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.jdom2.Element;

public class ChildFilterParser extends FilterParser {
    
    private final FilterModule child;
    
    public ChildFilterParser(Element element) {
        super(element);
        this.child = FilterModuleBuilder.getFilter(element.getChildren().get(0));
    }

    public FilterModule getChild() {
        return child;
    }
}
