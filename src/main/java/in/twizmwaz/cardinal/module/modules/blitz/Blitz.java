package in.twizmwaz.cardinal.module.modules.blitz;


import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class Blitz implements Module{

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    private static String title = null;
    private boolean broadcastLives;
    private int lives;
    private int time;

    protected Blitz(final String title, final boolean broadcastLives, final int lives, final int time){
        this.title = title;
        this.broadcastLives = broadcastLives;
        this.lives = lives;
        this.time = time;
    }

    private HashMap<Player, Integer> plasyerLives = new HashMap<Player, Integer>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        plasyerLives.put(player, plasyerLives.get(player) - 1);
        if (getLives(player) == 0){
            TeamUtils.getTeamById("observers").add(player, true, "You ran out of lives!");
        }
    }

    @EventHandler
    public void onPgmSpawn(PgmSpawnEvent event){
        Player player = event.getPlayer();
        if (!plasyerLives.containsKey(player)){
            plasyerLives.put(player, lives);
        }
        if(broadcastLives){
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + getLives(player) + ChatColor.AQUA + "" + ChatColor.BOLD + " Lives Remaining");

        }
    }

    private int getLives(Player player){
        return plasyerLives.get(player);
    }

    public static String getTitle(){
        return title;
    }
}
