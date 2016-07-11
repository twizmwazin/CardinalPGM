package in.twizmwaz.cardinal.util;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.rotation.Rotation;
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
            global = parse(globalFile);
        } catch (JDOMException | IOException e) {
            global = new Document(new Element("map"));
        }
    }

    public static Document parse(File file) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(file);
    }

    public static Document parseMap(File file) throws JDOMException, IOException {
        Document original = parse(file);
        merge(original, global);
        List<String> toInclude = Lists.newArrayList();
        for (Element include : original.getRootElement().getChildren("include")) {
            String src = include.getAttributeValue("src");
            if (!src.equals("")) toInclude.add(src.substring(src.lastIndexOf('/') + 1));
        }
        for (String include : toInclude) {
            File including = Rotation.getInclude(include);

            if (including != null)
                merge(original, including);
            else
                Bukkit.getLogger().log(Level.WARNING, "File '" + include + "' was not found nor included!");
        }
        return original;
    }

    public static Document merge(Document original, File copy) {
        try {
            return merge(original, parseMap(copy));
        } catch (JDOMException | IOException ignored) {
            return original;
        }
    }

    public static Document merge(Document original, Document copy) {
        if (copy != null) {
            for (Element element : copy.getRootElement().getChildren()) {
                original.getRootElement().addContent(element.clone().detach());
            }
        }
        return original;
    }

}
