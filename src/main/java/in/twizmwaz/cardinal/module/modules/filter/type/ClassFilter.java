package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ClassFilterParser;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class ClassFilter extends FilterModule {

    private String classModule;

    public ClassFilter(ClassFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.classModule = parser.getClassModule();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Player) {
                if (ClassModule.getClassByPlayer((Player) object).getName().equalsIgnoreCase(classModule))
                    return ALLOW;
                return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
