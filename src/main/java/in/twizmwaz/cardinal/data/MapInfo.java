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
        for (Element author : root.getChild("authors").getChildren()) {
            if (author.hasAttributes()) {
                authors.add(new Contributor(author.getText(), author.getAttribute("contribution").getValue()));
            } else {
                authors.add(new Contributor(author.getText()));
            }
        }
        contributors = new ArrayList<>();
        try {
            for (Element author : root.getChild("contributors").getChildren()) {
                if (author.hasAttributes()) {
                    contributors.add(new Contributor(author.getText(), author.getAttribute("contribution").getValue()));
                } else {
                    contributors.add(new Contributor(author.getText()));
                }
            }
        } catch (NullPointerException ex) {

        }
        rules = new ArrayList<>();
        try {
            for (Element rule : root.getChild("rules").getChildren()) {
                rules.add(rule.getValue());
            }
        } catch (NullPointerException ex) {

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
