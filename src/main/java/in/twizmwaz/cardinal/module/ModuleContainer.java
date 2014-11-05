package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.name.Name;
import in.twizmwaz.cardinal.util.XMLHandler;
import org.w3c.dom.Document;

import java.io.File;

/**
 * Created by kevin on 11/5/14.
 */
public class ModuleContainer {

    private Document document;
    private Match match;

    private Name name;


    public ModuleContainer(Match match) {
        document = new XMLHandler(new File("matches/" + match.getUUID().toString() + "/map.xml")).getDocument();
        registerAll();

    }

    private void registerAll() {
        name = new Name(document.getDocumentElement().getElementsByTagName("name").item(0));


    }

}
