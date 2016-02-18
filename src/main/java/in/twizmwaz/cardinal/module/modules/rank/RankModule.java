package in.twizmwaz.cardinal.module.modules.rank;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.event.RankChangeEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.module.modules.titleRespawn.TitleRespawn;
import in.twizmwaz.cardinal.rank.Rank;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

public class RankModule implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Rank rank : Rank.getDefaultRanks()) {
            if (rank.isDefaultRank() && !rank.contains(event.getPlayer().getUniqueId())) {
                rank.add(event.getPlayer().getUniqueId());
            }
        }
        for (Rank rank : Rank.getRanks(event.getPlayer().getUniqueId())) {
            for (String permission : rank.getPermissions()) {
                if (!event.getPlayer().hasPermission(permission)) {
                    GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).enablePermission(event.getPlayer(), permission);
                }
            }
        }
        for (Rank rank : Rank.getRanks(event.getPlayer().getUniqueId())) {
            for (String permission : rank.getDisabledPermissions()) {
                if (event.getPlayer().hasPermission(permission)) {
                    GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).disablePermission(event.getPlayer(), permission);
                }
            }
        }
    }

    @EventHandler
    public void onRankChange(RankChangeEvent event) {
        if (!event.isOnline()) return;
        Player player = event.getPlayer();

        Bukkit.getPluginManager().callEvent(new PlayerNameUpdateEvent(player));
        if (event.isAdding()) {
            for (String permission : event.getRank().getPermissions()) {
                GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).enablePermission(player, permission);
            }
            for (String permission : event.getRank().getDisabledPermissions()) {
                GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).disablePermission(player, permission);
            }
        } else {
            for (String permission : event.getRank().getPermissions()) {
                boolean keep = false;
                for (Rank rank : Rank.getRanks(player.getUniqueId())) {
                    if (rank.getPermissions().contains(permission)) {
                        keep = true;
                    }
                }
                if (!keep) {
                    GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).disablePermission(player, permission);
                }
            }
            for (String permission : event.getRank().getDisabledPermissions()) {
                boolean enable = false;
                for (Rank rank : Rank.getRanks(event.getPlayer().getUniqueId())) {
                    if (rank.getPermissions().contains(permission)) {
                        enable = true;
                    }
                    if (rank.getDisabledPermissions().contains(permission)) {
                        enable = false;
                        break;
                    }
                }
                if (enable) {
                    GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).enablePermission(player, permission);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        Bukkit.getPluginManager().callEvent(new PlayerNameUpdateEvent(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerNameUpdate(PlayerNameUpdateEvent event) {
        Player player = event.getPlayer();
        String prefix = Rank.getPrefix(player.getUniqueId());
        player.setDisplayName(prefix + Teams.getTeamColorByPlayer(player) + player.getName());
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(TitleRespawn.class).isDeadUUID(event.getPlayer().getUniqueId())) {
            player.setPlayerListName(prefix + ChatColor.DARK_GRAY + player.getName());
        } else {
            player.setPlayerListName(prefix + Teams.getTeamColorByPlayer(player) + player.getName());
        }
    }

}

