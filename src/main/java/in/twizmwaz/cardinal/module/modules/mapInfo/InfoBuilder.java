package in.twizmwaz.cardinal.module.modules.mapInfo;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.mapInfo.contributor.Contributor;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class InfoBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        Element root = match.getDocument().getRootElement();
        List<Contributor> authors;
        List<Contributor> contributors;
        List<String> rules;
        String name = root.getChild("name").getText();
        String version = root.getChild("version").getText();
        String objective = root.getChild("objective").getText();
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
        results.add(new Info(name, version, objective, authors, contributors, rules));
        return results;
    }

}
