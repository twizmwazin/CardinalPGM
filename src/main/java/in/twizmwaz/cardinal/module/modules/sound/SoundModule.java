package in.twizmwaz.cardinal.module.modules.sound;

import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.TeamUtils;
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
                player.playSound(player.getLocation(), Sound.PORTAL_TRAVEL, 1, 2);
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                if (TeamUtils.getTeamByPlayer(player) == event.getTeam()) {
                    player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 1);
                } else {
                    player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1, 1);
                }
            }
        }
    }

}
