package in.twizmwaz.cardinal.module.modules.rank;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.event.RankChangeEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.rank.Rank;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Rank rank : Rank.getDefaultRanks()) {
            if (rank.isDefaultRank() && !rank.contains(event.getPlayer().getUniqueId())) {
                rank.add(event.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onRankChange(RankChangeEvent event) {
        Player player = event.getPlayer();

        Bukkit.getPluginManager().callEvent(new PlayerNameUpdateEvent(player));
        if (event.isAdding()) {
            for (String permission : event.getRank().getPermissions()) {
                GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).enablePermission(player, permission);
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
        player.setPlayerListName(prefix + Teams.getTeamColorByPlayer(player) + player.getName());
    }
}

