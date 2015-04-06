package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.TimeFilterParser;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class TimeFilter extends FilterModule {

    private int time = 0;

    public TimeFilter(TimeFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.time = parser.getTime();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        return MatchTimer.getTimeInSeconds() >= this.time ? ALLOW : DENY;
    }

}