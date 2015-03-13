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
            File path = file.getParentFile();
            String source = include.getAttributeValue("src");
            while (source.startsWith("../")) {
                source = source.replace("../", "");
                path = path.getParentFile();
            }
            File including = new File(path, source);
            try {
                for (Element element : parse(including).getRootElement().getChildren()) {
                    original.getRootElement().addContent(element.clone().detach());
                }
            } catch (JDOMException | IOException e) {
                Bukkit.getLogger().log(Level.WARNING, "File '" + including.getName() + "' was not found nor included!");
            }
        }
        return original;
    }
}
