package in.twizmwaz.cardinal.filter;

import org.bukkit.event.Event;

public abstract class Filter {

    /**
     * @param event The event which will be filters
     * @return The state of the filter.
     */
    public abstract FilterState evaluate(final Event event);

}
