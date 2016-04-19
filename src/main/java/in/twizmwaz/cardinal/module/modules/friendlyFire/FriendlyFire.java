package in.twizmwaz.cardinal.module.modules.friendlyFire;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.scoreboard.ScoreboardModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FriendlyFire implements Module {

    private boolean enabled;
    private boolean arrowReturn;

    protected FriendlyFire(boolean enabled, boolean arrowReturn) {
        this.enabled = enabled;
        this.arrowReturn = arrowReturn;
        if (enabled) {
            for (TeamModule team : Teams.getTeams()) {
                for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
                    scoreboard.getScoreboard().getTeam(team.getId()).setAllowFriendlyFire(false);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        if (!enabled) return;
        if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() == event.getEntity()){
            event.setCancelled(true);
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
