package in.twizmwaz.cardinal;

import in.twizmwaz.cardinal.rank.Rank;
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
        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());
        try {
            if (file.createNewFile()) {
                Cardinal.getInstance().getLogger().info("Database file not found, creating...");
                out.output(document, new FileWriter(file));
                return true;
            } else {
                out.output(document, new FileWriter(file));
                return true;
            }
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

    public void put(Rank rank, UUID player) {
        Element rankElement = getRankElement(rank);
        if (!rankContainsPlayer(rankElement, player)) {
            Element newPlayer = new Element("player").setAttribute(new Attribute("uuid", player.toString()));
            rankElement.addContent(newPlayer);
        }
    }

    public boolean get(Rank rank, UUID player) {
        return rankContainsPlayer(getRankElement(rank), player);
    }

    public void remove(Rank rank, UUID player) {
        Element rankElement = getRankElement(rank);
        if (rankContainsPlayer(rankElement, player)) {
            rankElement.removeContent(getRankPlayer(rankElement, player));
        }
    }

    private Element getPlayerElement(OfflinePlayer player) {
        for (Element element : document.getRootElement().getChildren()) {
            if (player.getUniqueId().equals(UUID.fromString(element.getAttributeValue("uuid")))) return element;
        }
        Element newPlayer = new Element("player").setAttribute(new Attribute("uuid", player.getUniqueId().toString()));
        document.getRootElement().addContent(newPlayer);
        return newPlayer;
    }

    private Element getRankElement(Rank rank) {
        for (Element element : document.getRootElement().getChildren("rank")) {
            if (rank.getName().equals(element.getAttributeValue("name"))) return element;
        }
        Element newRank = new Element("rank").setAttribute(new Attribute("name", rank.getName()));
        document.getRootElement().addContent(newRank);
        return newRank;
    }

    private Element getKey(Element element, String key) {
        for (Element child : element.getChildren("data")) {
            if (key.equals(child.getAttributeValue("key"))) return child;
        }
        Element newElement = new Element(("data")).setAttribute(new Attribute("key", key)).setAttribute("value", "");
        element.addContent(newElement);
        return newElement;
    }

    private boolean rankContainsPlayer(Element element, UUID player) {
        return getRankPlayer(element, player) != null;
    }

    private Element getRankPlayer(Element element, UUID player) {
        for (Element child : element.getChildren("player")) {
            if (player.equals(UUID.fromString(child.getAttributeValue("uuid")))) return child;
        }
        return null;
    }

}
