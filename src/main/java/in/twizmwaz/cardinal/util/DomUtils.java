package in.twizmwaz.cardinal.util;

import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class DomUtils {

    public static Document parse(File file) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document original = saxBuilder.build(file);
        Path repo = file.getParentFile().getParentFile().toPath();
        for (Element include : original.getRootElement().getChildren("include")) {
            try {
                Path included;
                try {
                    included = repo.resolve(include.getAttributeValue("src")).normalize();
                    for (Element element : parse(included.toFile()).getRootElement().getChildren()) {
                        original.getRootElement().addContent(element.detach());
                    }
                } catch (FileNotFoundException e) {
                    included = file.getParentFile().toPath().resolve(include.getAttributeValue("src")).normalize();
                    for (Element element : parse(included.toFile()).getRootElement().getChildren()) {
                        original.getRootElement().addContent(element.detach());
                        Bukkit.getLogger().info(element.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return original;
    }
}
