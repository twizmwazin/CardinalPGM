package in.twizmwaz.cardinal.module.modules.friendlyFire;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.scoreboard.ScoreboardModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class FriendlyFire implements Module {

    private Match match;
    private boolean arrowReturn;

    protected FriendlyFire(Match match, boolean enabled, boolean arrowReturn) {
        this.match = match;
        this.arrowReturn = arrowReturn;
        if (enabled) {
            for (TeamModule team : TeamUtils.getTeams()) {
                for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
                    scoreboard.getScoreboard().getTeam(team.getId()).setAllowFriendlyFire(false);
                }
            }
        }
    }

    @EventHandler
    public void onBowShootEvent(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            event.getEntity().setMetadata("team", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), TeamUtils.getTeamByPlayer(((Player) event.getEntity()).getPlayer()).getId()));
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                if (event.getDamager().hasMetadata("team")) {
                    Player shooter = (Player) ((Arrow) event.getDamager()).getShooter();
                    if (TeamUtils.getTeamByPlayer(shooter) == TeamUtils.getTeamById(event.getDamager().getMetadata("team").get(0).toString())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
