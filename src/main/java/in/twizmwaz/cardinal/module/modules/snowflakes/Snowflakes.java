package in.twizmwaz.cardinal.module.modules.snowflakes;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class Snowflakes implements Module {

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
        if (killer != null && TeamUtils.getTeamByPlayer(event.getPlayer()) != TeamUtils.getTeamByPlayer(event.getKiller())) {
            int snowflakes = 1;
            double multiplier = 1.0;
            killer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "+" + (int) (snowflakes * multiplier) + ChatColor.WHITE + " Snowflakes" + ChatColor.DARK_PURPLE + " | " + ChatColor.GOLD + "" + ChatColor.ITALIC + multiplier + "x" + ChatColor.DARK_PURPLE + " | " + ChatColor.GRAY + "killed " + TeamUtils.getTeamByPlayer(event.getPlayer()).getColor() + event.getPlayer().getName());
            if (Cardinal.getCardinalDatabase().get(killer, "snowflakes").equals("")) {
                Cardinal.getCardinalDatabase().put(killer, "snowflakes", (int) (snowflakes * multiplier) + "");
            } else {
                Cardinal.getCardinalDatabase().put(killer, "snowflakes", (Integer.parseInt(Cardinal.getCardinalDatabase().get(killer, "snowflakes")) + (int) (snowflakes * multiplier)) + "");
            }
        }
    }

}
