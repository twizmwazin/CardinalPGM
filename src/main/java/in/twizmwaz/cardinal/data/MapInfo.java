package in.twizmwaz.cardinal.data;

import org.jdom2.Document;
import org.jdom2.Element;

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
        Element root = doc.getRootElement();
        name = root.getChild("name").getText();
        version = root.getChild("version").getText();
        objective = root.getChild("objective").getText();
        authors = new ArrayList<>();
        for (Element element : root.getChildren("authors")) {
            for (Element author : element.getChildren()) {
                if (author.hasAttributes()) {
                    authors.add(new Contributor(author.getText(), author.getAttribute("contribution").getValue()));
                } else {
                    authors.add(new Contributor(author.getText()));
                }
            }
        }
        contributors = new ArrayList<>();
        for (Element element : root.getChildren("contributors")) {
            for (Element author : element.getChildren()) {
                try {
                    contributors.add(new Contributor(author.getText(), author.getAttributeValue("contribution")));
                } catch (NullPointerException e) {
                }
            }

        }
        rules = new ArrayList<>();
        for (Element element : root.getChildren("rules")) {
            for (Element rule : element.getChildren()) {
                rules.add(rule.getValue());
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
