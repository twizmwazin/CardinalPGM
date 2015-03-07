package in.twizmwaz.cardinal.module.modules.stats;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MatchTracker {

    private final OfflinePlayer player;
    private final OfflinePlayer killer;
    private final ItemStack weapon;

    protected MatchTracker(OfflinePlayer player, OfflinePlayer killer, ItemStack weapon) {
        this.player = player;
        this.killer = killer;
        this.weapon = weapon;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public OfflinePlayer getKiller() {
        return killer;
    }

    public ItemStack getWeapon() {
        return weapon;
    }
}
