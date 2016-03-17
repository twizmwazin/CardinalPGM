package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public interface Kit extends Module {

    public void apply(Player player, Boolean force);

}
