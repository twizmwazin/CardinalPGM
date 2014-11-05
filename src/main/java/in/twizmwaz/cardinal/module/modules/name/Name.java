package in.twizmwaz.cardinal.module.modules.name;

import org.w3c.dom.Node;

/**
 * Created by kevin on 11/5/14.
 */
public class Name {

    private String name;

    public Name(Node node) {
        name = node.getTextContent();

    }

    public String getName() {
        return name;
    }

}
