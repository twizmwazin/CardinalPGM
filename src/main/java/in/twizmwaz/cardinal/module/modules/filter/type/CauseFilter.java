package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.CauseFilterParser;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class CauseFilter extends FilterModule {

    private final EventCause cause;

    public CauseFilter(final CauseFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.cause = parser.getCause();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            EventCause eventCause = null;
            if (object instanceof Player) {
                eventCause = EventCause.PLAYER;
            } else if (object instanceof TNTPrimed) {
                return cause.equals(EventCause.TNT) ? ALLOW : DENY;
            }
            if (cause.equals(eventCause))
                return ALLOW;
            else if (eventCause != null)
                return DENY;
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

    public enum EventCause {

        /**
         * The event was generated be a player action.
         */
        PLAYER(),
        /**
         * The event was generated by TNT.
         */
        TNT();

        public static EventCause getEventCause(String string) {
            switch (string.toLowerCase().replaceAll(" ", "")) {
                case "player":
                    return PLAYER;
                case "tnt":
                case "explosion":
                    return TNT;
                default:
                    return null;
            }
        }

    }

}
