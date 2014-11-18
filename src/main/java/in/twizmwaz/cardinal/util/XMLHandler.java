package in.twizmwaz.cardinal.util;

import org.bukkit.Bukkit;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by kevin on 11/5/14.
 */
public class XMLHandler {

    private Document document;

    public XMLHandler(File xmlFile) {
        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
        } catch (FactoryConfigurationError e) {
            Bukkit.getLogger().log(Level.WARNING, "Error in processing XML file");
        } catch (ParserConfigurationException e) {
            Bukkit.getLogger().log(Level.WARNING, "Error in processing XML file");
        } catch (SAXException e) {
            Bukkit.getLogger().log(Level.WARNING, "Error in processing XML file, is there an issue in formatting?");
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "Error in processing XML file");
            e.printStackTrace();
        }
    }

    public Document getDocument() {
        return document;
    }

    public static List<Node> nodeListToList(NodeList nodeList) {
        List<Node> results = new ArrayList<Node>();
        for ( int i = 0; i < nodeList.getLength(); i++) {
            results.add(nodeList.item(i));
        }
        return results;
    }
}
