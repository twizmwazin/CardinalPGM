package in.twizmwaz.cardinal.module.modules.itemDrop;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDrop implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning()
                && (!team.isPresent() || !team.get().isObserver())) {
            dump(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSwitchTeam(PlayerChangeTeamEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning()
                && (!team.isPresent() || !team.get().isObserver())) {
            dump(event.getPlayer());
        }
    }

    private void dump(Player player) {
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            if (stack != null && stack.getType() != Material.AIR) {
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 0.5, 0), stack);
            }
        }
        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack != null && stack.getType() != Material.AIR) {
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 0.5, 0), stack);
            }
        }
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

}
