package in.twizmwaz.cardinal.module.modules.titleRespawn;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

import in.twizmwaz.cardinal.util.Strings;
import org.jdom2.Element;

public class TitleRespawnBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TitleRespawn> load(Match match) {
        ModuleCollection<TitleRespawn> results = new ModuleCollection<>();
        Element head = match.getDocument().getRootElement().getChild("respawn");
        if (head != null) {
            for (Element element : match.getDocument().getRootElement().getChildren("respawn")) {

                boolean auto = Boolean.parseBoolean(element.getAttributeValue("auto", "false"));
                boolean blackout = Boolean.parseBoolean(element.getAttributeValue("blackout", "false"));
                boolean spectate = Boolean.parseBoolean(element.getAttributeValue("spectate", "false"));
                boolean bed = Boolean.parseBoolean(element.getAttributeValue("bed", "false"));

                String message = element.getAttributeValue("message") != null ? element.getAttributeValue("message") : null;
                for (Element subElement : element.getChildren("message")) {
                    message = subElement.getAttributeValue("message") != null ? subElement.getAttributeValue("message") : null;
                }

                //TODO Parse JSON
                if (message != null) message = Strings.getTechnicalName(message.replace(".", "_"));

                int delay = Strings.timeStringToSeconds(element.getAttributeValue("delay", "2"));
                if (delay < 1) delay = 1;

                return new ModuleCollection<>(new TitleRespawn(delay, auto, blackout, spectate, bed, message));
            }
            return results;
        } else {
            return new ModuleCollection<>(new TitleRespawn(2, false, false, false, false, null));
        }
    }

}