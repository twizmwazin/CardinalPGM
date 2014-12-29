package in.twizmwaz.cardinal.module.modules.team;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.JoinType;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.teams.PgmTeam;
import in.twizmwaz.cardinal.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamModule implements Module {

    private final JavaPlugin plugin;
    private final Match match;

    protected TeamModule(Match match) {
        this.plugin = GameHandler.getGameHandler().getPlugin();
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.getPlayer().setScoreboard(match.getScoreboard());
        GameHandler.getGameHandler().getMatch().getTeamById("observers").add(player, JoinType.FORCED);
        PlayerUtil.resetPlayer(player);
        player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerKickEvent event) {
        removePlayer(event.getPlayer());
    }

    private void removePlayer(Player player) {
        for (PgmTeam team : match.getTeams()) {
            if (team.hasPlayer(player)) {
                team.remove(player);
            }
        }
    }


}
