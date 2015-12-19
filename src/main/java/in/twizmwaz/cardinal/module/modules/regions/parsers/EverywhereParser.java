package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.jdom2.Element;

public class EverywhereParser extends RegionParser {

    public EverywhereParser(Element element) {
        super(element.getAttributeValue("name") != null ? element.getAttributeValue("name") : element.getAttributeValue("id"));
    }

}
