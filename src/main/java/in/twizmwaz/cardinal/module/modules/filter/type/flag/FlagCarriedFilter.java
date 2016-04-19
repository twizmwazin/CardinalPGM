package in.twizmwaz.cardinal.module.modules.filter.type.flag;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.FlagFilterPostParser;

public class FlagCarriedFilter extends FlagFilterPost {

    public FlagCarriedFilter(FlagFilterPostParser parser) {
        super(parser);
    }

    @Override
    public FilterState evaluate(Object... objects) {
        return (this.getPost() == null || this.getFlag().getPost().equals(this.getPost()))
                && this.getFlag().isCarried() ? FilterState.ALLOW : FilterState.DENY;
    }

}
