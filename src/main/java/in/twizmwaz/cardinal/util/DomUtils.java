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
        Bukkit.getLogger().info("1");
        for (Element include : original.getRootElement().getChildren("include")) {
            Bukkit.getLogger().info("2");
            File path = file.getParentFile();
            Bukkit.getLogger().info("3");
            String source = include.getAttributeValue("src");
            Bukkit.getLogger().info("4");
            while (source.contains("../")) {
                source = source.replace("../", "");
                path = path.getParentFile();
            }
            Bukkit.getLogger().info("5");
            File including = new File(path, source);
            Bukkit.getLogger().info("6");
            try {
                for (Element element : parse(including).getRootElement().getChildren()) {
                    Bukkit.getLogger().info(element.getName() + "");
                    original.getRootElement().addContent(element.detach());
                }
            } catch (JDOMException | IOException e) {
                Bukkit.getLogger().log(Level.WARNING, "File '" + including.getName() + "' was not included correctly!");
            }
        }
        return original;
    }
}
