package in.twizmwaz.cardinal;

import org.bukkit.OfflinePlayer;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public final class Database {
    
    private final Document document;

    private Database(Document document) {
        this.document = document;
    }
    
    private Database() {
        this.document = new Document(new Element("database"));
    }
    
    public static Database newInstance(File file) {
        Database newDatabase = new Database();
        newDatabase.save(file);
        return newDatabase;
    }
    
    public static Database loadFromFile(File file) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        return new Database(saxBuilder.build(file));
    }
    
    public boolean save(File file) {
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        try {
            outputter.output(document, new FileWriter(file));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void put(OfflinePlayer player, String key, String value) {
        getKey(getPlayerElement(player), key).setAttribute("value", value);
    }
    
    public String get(OfflinePlayer player, String key) {
        return getKey(getPlayerElement(player), key).getAttributeValue("value");
    }
    
    private Element getPlayerElement(OfflinePlayer player) {
        for (Element element : document.getRootElement().getChildren()) {
            if (player.getUniqueId().equals(UUID.fromString(element.getAttributeValue("uuid")))) return element;
        }
        Element newPlayer = new Element("player").setAttribute(new Attribute("uuid", player.getUniqueId().toString()));
        document.getRootElement().addContent(newPlayer);
        return newPlayer;
    }
    
    private Element getKey(Element element, String key) {
        for (Element child : element.getChildren("data")) {
            if (key.equals(child.getAttributeValue("key"))) return child;
        }
        Element newElement = new Element(("data")).setAttribute(new Attribute("key", key)).setAttribute("value", "");
        element.addContent(newElement);
        return newElement;
    }
    
}
