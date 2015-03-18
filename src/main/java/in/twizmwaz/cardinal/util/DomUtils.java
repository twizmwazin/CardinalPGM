package in.twizmwaz.cardinal.util;

import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DomUtils {

    public static Document parse(File file) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document original = saxBuilder.build(file);
        for (Element include : original.getRootElement().getChildren("include")) {
            boolean found = false;
            File path = file.getParentFile();
            String source = include.getAttributeValue("src");
            File including = new File(path, source);
            if (including.exists()) {
                found = true;
                try {
                    for (Element element : parse(including).getRootElement().getChildren()) {
                        original.getRootElement().addContent(element.clone().detach());
                    }
                } catch (JDOMException | IOException ignored) {}
            } else {
                while (source.startsWith("../")) {
                    source = source.replace("../", "");
                }
                including = new File(path, source);
                if (including.exists()) {
                    found = true;
                    try {
                        for (Element element : parse(including).getRootElement().getChildren()) {
                            original.getRootElement().addContent(element.clone().detach());
                        }
                    } catch (JDOMException | IOException ignored) {
                    }
                }
                including = new File(path.getParentFile(), source);
                if (including.exists()) {
                    found = true;
                    try {
                        for (Element element : parse(including).getRootElement().getChildren()) {
                            original.getRootElement().addContent(element.clone().detach());
                        }
                    } catch (JDOMException | IOException ignored) {
                    }
                }
            }
            if (!found) Bukkit.getLogger().log(Level.WARNING, "File '" + including.getName() + "' was not found nor included!");
        }
        return original;
    }
}
