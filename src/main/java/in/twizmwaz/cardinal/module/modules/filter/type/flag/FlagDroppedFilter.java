package in.twizmwaz.cardinal.module.modules.filter.type.flag;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.FlagFilterPostParser;

public class FlagDroppedFilter extends FlagFilterPost {

    public FlagDroppedFilter(FlagFilterPostParser parser) {
        super(parser);
    }

    @Override
    public FilterState evaluate(Object... objects) {
        return (this.getPost() == null || this.getFlag().getPost().equals(this.getPost()))
                && this.getFlag().isDropped() ? FilterState.ALLOW : FilterState.DENY;
    }
}
