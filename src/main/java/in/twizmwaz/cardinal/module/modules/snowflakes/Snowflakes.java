package in.twizmwaz.cardinal.module.modules.snowflakes;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.SnowflakeChangeEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class Snowflakes implements Module {

    public enum ChangeReason {
        PLAYER_KILL(), WOOL_TOUCH(), WOOL_PLACE(), CORE_LEAK(), TEAM_WIN(), TEAM_LOYAL()
    }

    public Snowflakes() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && TeamUtils.getTeamByPlayer(event.getPlayer()) != TeamUtils.getTeamByPlayer(event.getKiller())) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getKiller(), ChangeReason.PLAYER_KILL, 1, event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (TeamUtils.getTeamByPlayer(player) != null && !TeamUtils.getTeamByPlayer(player).isObserver() && event.getTeam() == TeamUtils.getTeamByPlayer(player)) {
                Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.TEAM_WIN, 15, TeamUtils.getTeamByPlayer(player).getCompleteName()));
            } else if (TeamUtils.getTeamByPlayer(player) != null && !TeamUtils.getTeamByPlayer(player).isObserver() && event.getTeam() != TeamUtils.getTeamByPlayer(player)) {
                Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(player, ChangeReason.TEAM_LOYAL, 5, TeamUtils.getTeamByPlayer(player).getCompleteName()));
            }
        }
    }

    @EventHandler
    public void onSnowflakeChange(SnowflakeChangeEvent event) {
        String reason;
        if (event.getChangeReason().equals(ChangeReason.PLAYER_KILL)) {
            reason = "killed " + TeamUtils.getTeamColorByPlayer(Bukkit.getOfflinePlayer(event.get(0))) + event.get(0);
        } else if (event.getChangeReason().equals(ChangeReason.WOOL_TOUCH)) {
            reason = "picked up " + event.get(0);
        } else if (event.getChangeReason().equals(ChangeReason.WOOL_PLACE)) {
            reason = "placed " + event.get(0);
        } else if (event.getChangeReason().equals(ChangeReason.CORE_LEAK)) {
            reason = "you broke a piece of " + event.get(0);
        } else if (event.getChangeReason().equals(ChangeReason.TEAM_WIN)) {
            reason = "your team (" + event.get(0) + ChatColor.GRAY + ") won";
        } else if (event.getChangeReason().equals(ChangeReason.TEAM_LOYAL)) {
            reason = "you were loyal to your team (" + event.get(0) + ChatColor.GRAY + ")";
        } else {
            reason = "unknown reason";
        }
        event.getPlayer().sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "+" + event.getFinalAmount() + ChatColor.AQUA + " Snowflakes" + ChatColor.DARK_PURPLE + " | " + ChatColor.GOLD + "" + ChatColor.ITALIC + event.getMultiplier() + "x" + ChatColor.DARK_PURPLE + " | " + ChatColor.GRAY + reason).getMessage(event.getPlayer().getLocale()));
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1.5F);
        if (Cardinal.getCardinalDatabase().get(event.getPlayer(), "snowflakes").equals("")) {
            Cardinal.getCardinalDatabase().put(event.getPlayer(), "snowflakes", event.getFinalAmount() + "");
        } else {
            Cardinal.getCardinalDatabase().put(event.getPlayer(), "snowflakes", (NumUtils.parseInt(Cardinal.getCardinalDatabase().get(event.getPlayer(), "snowflakes")) + event.getFinalAmount()) + "");
        }
    }

}
