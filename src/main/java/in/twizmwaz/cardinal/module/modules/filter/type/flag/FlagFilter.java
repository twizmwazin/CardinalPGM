package in.twizmwaz.cardinal.module.modules.filter.type.flag;

import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.FlagFilterParser;

public class FlagFilter extends FilterModule {

    private FlagObjective flag;
    private FlagFilterParser parser;

    public FlagFilter(FlagFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.parser = parser;
    }

    public FlagObjective getFlag() {
        if (this.flag == null) this.flag = parser.getFlag();
        return flag;
    }

    @Override
    public FilterState evaluate(Object... objects) {
        return FilterState.ABSTAIN;
    }

}
