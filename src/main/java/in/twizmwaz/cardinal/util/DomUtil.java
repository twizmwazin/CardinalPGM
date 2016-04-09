package in.twizmwaz.cardinal.util;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.Cardinal;
import org.bukkit.Bukkit;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;

public class DomUtil {

    private static Document global;

    static {
        try {
            File globalFile = new File(Cardinal.getInstance().getConfig().getString("repo") + "/global.xml");
            Files.copy(Cardinal.getInstance().getResource("global.xml"), globalFile.toPath(), (CopyOption) StandardCopyOption.REPLACE_EXISTING);
            SAXBuilder saxBuilder = new SAXBuilder();
            global = saxBuilder.build(globalFile);
        } catch (JDOMException | IOException e) {
        }
    }

    public static Document parse(File file) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document original = saxBuilder.build(file);
        merge(original, global);
        List<String> toInclude = Lists.newArrayList();
        for (Element include : original.getRootElement().getChildren("include"))
            toInclude.add(include.getAttributeValue("src"));
        for (String include : toInclude) {
            boolean found = false;
            File path = file.getParentFile();
            File including = new File(path, include);
            if (including.exists()) {
                found = true;
                merge(original, including);
            } else {
                while (include.startsWith("../")) {
                    include = include.replace("../", "");
                }
                including = new File(path, include);
                if (including.exists()) {
                    found = true;
                    merge(original, including);
                }
                including = new File(path.getParentFile(), include);
                if (including.exists()) {
                    found = true;
                    merge(original, including);
                }
            }
            if (!found)
                Bukkit.getLogger().log(Level.WARNING, "File '" + including.getName() + "' was not found nor included!");
        }
        return original;
    }

    public static Document merge(Document original, File copy) {
        try {
            return merge(original, parse(copy));
        } catch (JDOMException | IOException ignored) {
            return original;
        }
    }

    public static Document merge(Document original, Document copy) {
        for (Element element : copy.getRootElement().getChildren()) {
            original.getRootElement().addContent(element.clone().detach());
        }
        return original;
    }

}
