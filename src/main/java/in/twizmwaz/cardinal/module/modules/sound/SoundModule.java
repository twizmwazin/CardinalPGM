package in.twizmwaz.cardinal.module.modules.sound;

import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveUncompleteEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class SoundModule implements Module {

    protected SoundModule() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                TeamModule team = Teams.getTeamByPlayer(player).get();
                if (team.isObserver() ||
                        ((event.getObjective() instanceof WoolObjective || event.getObjective() instanceof HillObjective) && team.equals(event.getObjective().getTeam())) ||
                        ((event.getObjective() instanceof CoreObjective || event.getObjective() instanceof DestroyableObjective) && !team.equals(event.getObjective().getTeam()))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.7f, 2f);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 0.8f, 0.8f);
                }
            }
        }
    }

    @EventHandler
    public void onObjectiveUncomplete(ObjectiveUncompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                TeamModule team = Teams.getTeamByPlayer(player).get();
                if (team.isObserver() || !team.equals(event.getOldTeam())) {
                    player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.7f, 2f);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 0.8f, 0.8f);
                }
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                if (Teams.getTeamByPlayer(player) == event.getTeam()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
                }
            }
        }
    }

}
