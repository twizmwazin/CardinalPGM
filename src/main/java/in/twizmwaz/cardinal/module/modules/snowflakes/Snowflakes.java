package in.twizmwaz.cardinal.module.modules.snowflakes;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
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
        PLAYER_KILL(), OBJECTIVE_TOUCH(), OBJECTIVE_COMPLETE(), TEAM_WIN(), TEAM_LOYAL()
    }

    public Snowflakes() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        Player killer = null;
        if (event.getKiller() != null) {
            killer = event.getKiller();
        } else if (event.getPlayerSpleefEvent() != null && event.getPlayerSpleefEvent().getSpleefer().isOnline()) {
            killer = (Player) event.getPlayerSpleefEvent().getSpleefer();
        }
        if (killer != null && TeamUtils.getTeamByPlayer(event.getPlayer()) != TeamUtils.getTeamByPlayer(killer)) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(killer, ChangeReason.PLAYER_KILL, 1, 1.0, event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onSnowflakeChange(SnowflakeChangeEvent event) {
        String reason;
        if (event.getChangeReason().equals(ChangeReason.PLAYER_KILL)) {
            reason = "killed " + TeamUtils.getTeamByPlayer(Bukkit.getPlayer(event.get(0))).getColor() + event.get(0);
        } else {
            reason = "unknown reason";
        }
        event.getPlayer().sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "+" + event.getFinalAmount() + ChatColor.WHITE + " Snowflakes" + ChatColor.DARK_PURPLE + " | " + ChatColor.GOLD + "" + ChatColor.ITALIC + event.getMultiplier() + "x" + ChatColor.DARK_PURPLE + " | " + ChatColor.GRAY + reason).getMessage(event.getPlayer().getLocale()));
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1.5F);
        if (Cardinal.getCardinalDatabase().get(event.getPlayer(), "snowflakes").equals("")) {
            Cardinal.getCardinalDatabase().put(event.getPlayer(), "snowflakes", event.getFinalAmount() + "");
        } else {
            Cardinal.getCardinalDatabase().put(event.getPlayer(), "snowflakes", (NumUtils.parseInt(Cardinal.getCardinalDatabase().get(event.getPlayer(), "snowflakes")) + event.getFinalAmount()) + "");
        }
    }

}
