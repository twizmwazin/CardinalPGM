package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.CauseFilterParser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class CauseFilter extends FilterModule {

    private final EventCause cause;

    public CauseFilter(final CauseFilterParser parser) {
        super(parser.getName());
        this.cause = parser.getCause();
    }

    @Override
    public FilterState evaluate(final Object object) {
        EventCause eventCause = null;
        if (object instanceof Player) {
            eventCause = EventCause.PLAYER;
        } else if (object instanceof Entity) {
            if (((Entity) object).getType().equals(EntityType.PRIMED_TNT)) {
                eventCause = EventCause.TNT;
            }
        }
        if (cause.equals(eventCause)) return ALLOW;
        else if (eventCause != null) return DENY;
        else return ABSTAIN;
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
                    return TNT;
                default:
                    return null;
            }
        }

    }

}
