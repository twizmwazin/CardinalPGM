package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ClassFilterParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class ClassFilter extends FilterModule {

    private String classModule;

    public ClassFilter(ClassFilterParser parser) {
        super(parser.getName());
        this.classModule = parser.getClassModule();
    }

    @Override
    public FilterState evaluate(final Object object) {
        if (object instanceof Player) {
            if (ClassModule.getClassByPlayer((Player) object).getName().equalsIgnoreCase(classModule)) return ALLOW;
            return DENY;
        }
        return ABSTAIN;
    }

}
