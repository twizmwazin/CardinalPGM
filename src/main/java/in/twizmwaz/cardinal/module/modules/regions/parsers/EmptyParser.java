package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.jdom2.Element;

public class EmptyParser extends RegionParser {

    public EmptyParser(Element element) {
        super(element.getAttributeValue("name"));
    }

}
