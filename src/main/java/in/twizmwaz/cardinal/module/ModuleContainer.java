package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.match.Match;
import org.jdom2.Document;

/**
 * Created by kevin on 11/5/14.
 */
public class ModuleContainer {

    private Document document;
    private Match match;

    public ModuleContainer(Match match) {
        document = match.getDocument();
        registerAll();

    }

    private void registerAll() {

    }


}
