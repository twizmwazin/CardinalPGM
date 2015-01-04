package in.twizmwaz.cardinal.regions.parsers.modifiers;

import in.twizmwaz.cardinal.regions.Region;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 10/27/14.
 */
public class CombinationParser {

    private List<Region> regions = new ArrayList<>();

    public CombinationParser(Element element, Document document) {
        for (Element child : element.getChildren()) regions.add(Region.getRegion(child, document));
    }

    public List<Region> getRegions() {
        return regions;
    }

}
