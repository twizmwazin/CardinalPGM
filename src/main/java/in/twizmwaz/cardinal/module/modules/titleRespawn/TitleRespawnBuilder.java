package in.twizmwaz.cardinal.module.modules.titleRespawn;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.util.StringUtils;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.NORMAL)
public class TitleRespawnBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        Element head = match.getDocument().getRootElement().getChild("respawn");

        int delay = StringUtils.timeStringToSeconds(head.getAttributeValue("delay", "2s"));
        if (delay < 1) {
            delay = 1;
        }

        boolean auto = Boolean.parseBoolean(head.getAttributeValue("auto", "false"));
        boolean blackout = Boolean.parseBoolean(head.getAttributeValue("blackout", "false"));
        boolean spectate = Boolean.parseBoolean(head.getAttributeValue("spectate", "false"));
        boolean bed = Boolean.parseBoolean(head.getAttributeValue("bed", "false"));

        TitleRespawn module = new TitleRespawn(delay, auto, blackout, spectate, bed);
        return new ModuleCollection<>(module);
    }

}
