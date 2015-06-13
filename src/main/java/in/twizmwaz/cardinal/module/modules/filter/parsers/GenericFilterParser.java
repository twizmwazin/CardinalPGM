package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import org.jdom2.Element;

public class GenericFilterParser extends FilterParser {

    private final ModuleCollection<FilterModule> children;

    public GenericFilterParser(final Element element) {
        super(element);
        this.children = new ModuleCollection<>();
        for (Element child : element.getChildren()) {
            children.add(FilterModuleBuilder.getFilter(child));
        }
    }

    public ModuleCollection<FilterModule> getChildren() {
        return children;
    }
}
