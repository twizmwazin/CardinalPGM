package in.twizmwaz.cardinal.module.modules.friendlyFire;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.scoreboard.ScoreboardModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FriendlyFire implements Module {

    private Match match;
    private boolean arrowReturn;

    protected FriendlyFire(Match match, boolean enabled, boolean arrowReturn) {
        this.match = match;
        this.arrowReturn = arrowReturn;
        if (enabled) {
            for (TeamModule team : Teams.getTeams()) {
                for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
                    scoreboard.getScoreboard().getTeam(team.getId()).setAllowFriendlyFire(false);
                }
            }
        }
    }

    @EventHandler
    public void onBowShootEvent(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Optional<TeamModule> team = Teams.getTeamByPlayer(((Player) event.getEntity()).getPlayer());
            if (team.isPresent())
                event.getEntity().setMetadata("team", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), team.get().getId()));
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                if (event.getDamager().hasMetadata("team")) {
                    Player shooter = (Player) ((Arrow) event.getDamager()).getShooter();
                    if (Teams.getTeamByPlayer(shooter) == Teams.getTeamById(event.getDamager().getMetadata("team").get(0).toString())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        boolean proceed = false;
        for (PotionEffect effect : event.getPotion().getEffects()) {
            if (effect.getType().equals(PotionEffectType.POISON) || effect.getType().equals(PotionEffectType.BLINDNESS) ||
                    effect.getType().equals(PotionEffectType.CONFUSION) || effect.getType().equals(PotionEffectType.HARM) ||
                    effect.getType().equals(PotionEffectType.HUNGER) || effect.getType().equals(PotionEffectType.SLOW) ||
                    effect.getType().equals(PotionEffectType.SLOW_DIGGING) || effect.getType().equals(PotionEffectType.WITHER) ||
                    effect.getType().equals(PotionEffectType.WEAKNESS)) {
                proceed = true;
            }
        }
        if (proceed && event.getPotion().getShooter() instanceof Player && Teams.getTeamByPlayer((Player) event.getPotion().getShooter()) != null) {
            Optional<TeamModule> team = Teams.getTeamByPlayer((Player) event.getPotion().getShooter());
            for (LivingEntity affected : event.getAffectedEntities()) {
                if (affected instanceof Player && Teams.getTeamByPlayer((Player) affected) != null && Teams.getTeamByPlayer((Player) affected).equals(team) && !affected.equals(event.getPotion().getShooter())) {
                    event.setIntensity(affected, 0);
                }
            }
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
