package in.twizmwaz.cardinal.util;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.repository.repositories.Repository;
import org.apache.commons.io.FileUtils;
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

    private static Document global;

    static {
        try {
            File file = new File(Cardinal.getNewRepoPath(""), "global.xml");
            FileUtils.copyInputStreamToFile(Cardinal.getInstance().getResource("global.xml"), file);
            global = parse(file);
        } catch (JDOMException | IOException e) {
            global = new Document(new Element("map"));
            Cardinal.getInstance().getLogger().warning("global.xml could not be setup, this will affect gameplay!");
        }
    }

    public static Document parse(File file) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(file);
    }

    public static Document parseMap(Repository repo, File file) throws JDOMException, IOException {
        Document xml = parse(file);
        merge(xml, global);
        List<String> toInclude = Lists.newArrayList();
        for (Element include : xml.getRootElement().getChildren("include")) {
            String src = include.getAttributeValue("src");
            if (!src.equals("")) toInclude.add(src.substring(src.lastIndexOf('/') + 1));
        }
        include(repo, xml, "global.xml", file, false);
        for (String include : toInclude) {
            include(repo, xml, include, file, true);
        }
        return xml;
    }

    public static void include(Repository repo, Document doc, String include, File original, boolean warn) {
        File including = repo.getInclude(include);
        if (including != null)
            if (including != original) merge(repo, doc, including);
        else if (warn)
            Bukkit.getLogger().log(Level.WARNING, "File '" + include + "' was not found nor included!");
    }

    public static Document merge(Repository repo, Document original, File copy) {
        try {
            return merge(original, parseMap(repo, copy));
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
