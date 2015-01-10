package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class HoldingFilter extends Filter {

    private final Material material;

    public HoldingFilter(final Material material) {
        this.material = material;
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof PlayerEvent) {
            if (((PlayerEvent) event).getPlayer().getItemInHand().getType().equals(material)) return ALLOW;
            else return DENY;
        }
        return ABSTAIN;
    }

}
