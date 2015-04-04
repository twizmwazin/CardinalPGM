package in.twizmwaz.cardinal.chat;

import org.bukkit.plugin.Plugin;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LocaleHandler {
    
    private final Set<Document> documents;
    
    public LocaleHandler(Plugin plugin) throws JDOMException, IOException {
        this.documents = new HashSet<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        documents.add(saxBuilder.build(plugin.getResource("lang/en.xml")));
        documents.add(saxBuilder.build(plugin.getResource("lang/es.xml")));
        documents.add(saxBuilder.build(plugin.getResource("lang/fr.xml")));
        documents.add(saxBuilder.build(plugin.getResource("lang/it.xml")));
        documents.add(saxBuilder.build(plugin.getResource("lang/ko.xml")));
        documents.add(saxBuilder.build(plugin.getResource("lang/sv.xml")));
        documents.add(saxBuilder.build(plugin.getResource("lang/zh.xml")));
    }
    
    public Document getLocaleDocument(String locale) {
        for (Document document : documents) {
            if (locale.equals(document.getRootElement().getAttributeValue("lang"))) return document;
        }
        return getLocaleDocument("en");
    }
    
}
