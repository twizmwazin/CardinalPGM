package in.twizmwaz.cardinal.module.modules.filter.parsers;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.util.Flags;
import org.jdom2.Element;

public class FlagFilterParser extends FilterParser {

    private final String id;
    private FlagObjective flag;

    public FlagFilterParser(Element element) {
        super(element);
        this.id = element.getTextNormalize();
    }

    public FlagObjective getFlag() {
        if (this.flag == null) this.flag = Flags.getFlagById(id);
        return this.flag;
    }

}
