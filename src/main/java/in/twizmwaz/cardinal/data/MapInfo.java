package in.twizmwaz.cardinal.data;

import in.twizmwaz.cardinal.util.XMLHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 11/17/14.
 */
public class MapInfo {

    private String name;
    private String version;
    private String objective;
    private List<Contributor> authors;
    private List<Contributor> contributors;
    private List<String> rules;

    public MapInfo(Document doc) {
        name = doc.getDocumentElement().getElementsByTagName("name").item(0).getTextContent();
        version = doc.getDocumentElement().getElementsByTagName("version").item(0).getTextContent();
        objective = doc.getDocumentElement().getElementsByTagName("objective").item(0).getTextContent();
        authors = new ArrayList<>();
        for (Node node : XMLHandler.nodeListToList(doc.getDocumentElement().getElementsByTagName("authors").item(0).getChildNodes())) {
            if (node.hasAttributes()) {
                authors.add(new Contributor(node.getTextContent(), node.getAttributes().getNamedItem("contribution").getTextContent()));
            } else {
                authors.add(new Contributor(node.getTextContent()));
            }
        }
        contributors = new ArrayList<>();
        for (Node node : XMLHandler.nodeListToList(doc.getDocumentElement().getElementsByTagName("contributors").item(0).getChildNodes())) {
            if (node.hasAttributes()) {
                authors.add(new Contributor(node.getTextContent(), node.getAttributes().getNamedItem("contribution").getTextContent()));
            } else {
                authors.add(new Contributor(node.getTextContent()));
            }
        }
        rules = new ArrayList<>();
        if (doc.getDocumentElement().getElementsByTagName("rules").item(0) != null) {
            for (Node node : XMLHandler.nodeListToList(doc.getDocumentElement().getElementsByTagName("rules").item(0).getChildNodes())) {
                rules.add(node.getTextContent());
            }

        }
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getObjective() {
        return objective;
    }

    public List<Contributor> getAuthors() {
        return authors;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public List<String> getRules() {
        return rules;
    }
}
