package in.twizmwaz.cardinal.module.modules.blitz;


import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class Blitz implements Module {

    /**
     *
     * TODO:
     * - Add teams and team sizes to scoreboard
     * - Make Win condition
     * - Add countdown
     *
     */

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    private final JavaPlugin plugin;

    private static String title = null;
    private boolean broadcastLives;
    private int lives;
    private int time;

    protected Blitz(final String title, final boolean broadcastLives, final int lives, final int time){
        this.title = title;
        this.broadcastLives = broadcastLives;
        this.lives = lives;
        this.time = time;

        this.plugin = GameHandler.getGameHandler().getPlugin();
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        TeamModule team = TeamUtils.getTeamByPlayer(player);
        int oldMeta = player.getMetadata("lives").get(0).asInt();
        player.removeMetadata("lives", plugin);
        player.setMetadata("lives", new LazyMetadataValue(plugin, LazyMetadataValue.CacheStrategy.NEVER_CACHE, new BlitzLives(oldMeta - 1)));
        if (player.getMetadata("lives").get(0).asInt() == 0){
            TeamUtils.getTeamById("observers").add(player, true, ChatColor.RED + "You ran out of lives!");
            player.removeMetadata("lives", plugin);
        }
    }

    @EventHandler
    public void onPgmSpawn(PgmSpawnEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            Player player = event.getPlayer();
            if (TeamUtils.getTeams().contains(TeamUtils.getTeamByPlayer(player))) {
                if (!player.hasMetadata("lives")) {
                    player.setMetadata("lives", new LazyMetadataValue(plugin, LazyMetadataValue.CacheStrategy.NEVER_CACHE, new BlitzLives(lives)));
                }
                if (broadcastLives) {
                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You have " + ChatColor.RED + "" + ChatColor.BOLD + player.getMetadata("lives").get(0).asInt() + ChatColor.AQUA + ChatColor.BOLD + "" + " Lives Remaining");
                }
            }
        }
    }

    private int getLives(Player player){
        return player.getMetadata("lives").get(0).asInt();
    }

    public static String getTitle(){
        return title;
    }

}
