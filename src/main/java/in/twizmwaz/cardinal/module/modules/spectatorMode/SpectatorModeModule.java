package in.twizmwaz.cardinal.module.modules.spectatorMode;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpectatorModeModule implements Module {

    protected SpectatorModeModule() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_AIR) && !event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        Player player = event.getPlayer();
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (team.isPresent() && team.get().isObserver()) {
            if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(ChatColor.AQUA + ChatConstant.GENERIC_DISABLED_SPECTATOR_MODE.getMessage(player.getLocale()));
            } else {
                ItemStack item = event.getItem();
                if (item != null &&
                        item.hasItemMeta() &&
                        item.getItemMeta() != null &&
                        item.getItemMeta().hasDisplayName() &&
                        item.getItemMeta().getDisplayName() != null &&
                        item.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "" + ChatColor.BOLD + ChatConstant.UI_TOGGLE_SPECTATOR_MODE.getMessage(player.getLocale())) &&
                        item.getType().equals(Material.DIAMOND)) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(ChatColor.AQUA + ChatConstant.GENERIC_ENABLED_SPECTATOR_MODE.getMessage(player.getLocale()));
                }
            }
        }
    }

}
