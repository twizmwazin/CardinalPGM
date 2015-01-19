package in.twizmwaz.cardinal.module.modules.tntDefuse;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAttackEntityEvent;

import java.util.HashMap;
import java.util.UUID;

public class TntDefuse implements Module {
    private HashMap<String, UUID> tntPlaced = new HashMap<>();

    protected TntDefuse() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerAttackEntityEvent event) {
        if (event.getLeftClicked().getType().equals(EntityType.PRIMED_TNT)) {
            if (TntTracker.getWhoPlaced(event.getLeftClicked()) != null) {
                UUID player = TntTracker.getWhoPlaced(event.getLeftClicked());
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    if (TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player)) == TeamUtils.getTeamByPlayer(event.getPlayer())) {
                        if (!Bukkit.getPlayer(player).equals(event.getPlayer())) {
                            event.getLeftClicked().remove();
                            event.getPlayer().sendMessage(ChatColor.RED + "You defused " + TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player)).getColor() + Bukkit.getPlayer(player).getDisplayName() + ChatColor.RED + "'s TNT.");
                        }
                    } else {
                        ChatUtils.sendWarningMessage(event.getPlayer(), "You may not defuse enemy TNT.");
                    }
                } else {
                    ChatUtils.sendWarningMessage(event.getPlayer(), "You may not defuse enemy TNT.");
                }
            }
        }
    }
}
