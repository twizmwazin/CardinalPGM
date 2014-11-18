package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.time.Time;
import in.twizmwaz.cardinal.module.modules.time.Timelock;
import in.twizmwaz.cardinal.util.XMLHandler;
import org.w3c.dom.Document;

import java.io.File;

/**
 * Created by kevin on 11/5/14.
 */
public class ModuleContainer {

    private Document document;
    private Match match;

    private Timelock timelock;
    private Time time;


    public ModuleContainer(Match match) {
        document = new XMLHandler(new File("matches/" + match.getUUID().toString() + "/map.xml")).getDocument();
        registerAll();

    }

    private void registerAll() {
        if (document.getDocumentElement().getElementsByTagName("timelock").item(0) != null) {
            timelock = new Timelock(document.getDocumentElement().getElementsByTagName("timelock").item(0));
        }
        if (document.getDocumentElement().getElementsByTagName("time").item(0) != null) {
            time = new Time(document.getDocumentElement().getElementsByTagName("time").item(0));
        }


    }

    public Timelock getTimelock() {
        return timelock;
    }

    public Time getTime() {
        return time;
    }

}
