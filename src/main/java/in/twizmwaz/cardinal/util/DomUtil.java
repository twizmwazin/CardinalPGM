package in.twizmwaz.cardinal.util;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class DomUtil {

    public static Document parse(File file) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document original = saxBuilder.build(file);
        List<String> toInclude = Lists.newArrayList();
        for (Element include : original.getRootElement().getChildren("include"))
            toInclude.add(include.getAttributeValue("src"));
        for (String include : toInclude) {
            boolean found = false;
            File path = file.getParentFile();
            File including = new File(path, include);
            if (including.exists()) {
                found = true;
                try {
                    for (Element element : parse(including).getRootElement().getChildren()) {
                        original.getRootElement().addContent(element.clone().detach());
                    }
                } catch (JDOMException | IOException ignored) {
                }
            } else {
                while (include.startsWith("../")) {
                    include = include.replace("../", "");
                }
                including = new File(path, include);
                if (including.exists()) {
                    found = true;
                    try {
                        for (Element element : parse(including).getRootElement().getChildren()) {
                            original.getRootElement().addContent(element.clone().detach());
                        }
                    } catch (JDOMException | IOException ignored) {
                    }
                }
                including = new File(path.getParentFile(), include);
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
            if (!found)
                Bukkit.getLogger().log(Level.WARNING, "File '" + including.getName() + "' was not found nor included!");
        }
        return original;
    }
}
