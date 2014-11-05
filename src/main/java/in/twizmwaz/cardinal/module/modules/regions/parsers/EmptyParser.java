package in.twizmwaz.cardinal.module.modules.regions.parsers;

import org.w3c.dom.Node;

/**
 * Created by kevin on 10/26/14.
 */
public class EmptyParser {

    private String name;

    public EmptyParser(Node node) {
        this.name = node.getAttributes().getNamedItem("name").getNodeValue();
    }

    public String getName() {
        return name;
    }
}
