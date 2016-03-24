package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.entity.Player;

public interface Kit extends Module {

    void apply(Player player, Boolean force);

}
